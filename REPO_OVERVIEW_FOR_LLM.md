# TAG_WORKSPACE Repository Overview (for LLM-assisted changes)

## 1) Repository purpose (high-level)
This repository is a Java workspace with two Maven modules:

- `tag/`: the main board game project (engine, rule logic, players, UI, training utilities).
- `sandbox/`: experimental neural-network / genetic-algorithm code.

The root-level `README.md` is intentionally minimal; this document is the detailed quick-context file for future LLM edits.

---

## 2) Top-level layout

```text
TAG_WORKSPACE/
├─ README.md
├─ REPO_OVERVIEW_FOR_LLM.md
├─ brief.jpg
├─ tag/
│  ├─ pom.xml
│  ├─ training_ANN.nn
│  ├─ training_CNN.nn
│  ├─ tag-0.0.1-SNAPSHOT-jar-with-dependencies.jar
│  └─ src/main/java/
│     ├─ Main.java
│     └─ tag/
│        ├─ Controller.java
│        ├─ Data.java
│        ├─ Host.java
│        ├─ Player.java
│        ├─ Viewer.java
│        ├─ exception/
│        ├─ player/
│        │  └─ gametree/
│        ├─ trainning/
│        └─ ui/
└─ sandbox/
   ├─ pom.xml
   └─ src/main/java/sandbox/
      ├─ test.java
      └─ test2.java
```

---

## 3) Build system and dependencies

Both modules use Maven and include:
- `org.beykery:neuroph:2.92`
- `io.jenetics:jenetics:3.8.0`

### `tag/pom.xml`
- `groupId`: `hungike`
- `artifactId`: `tag`
- configured with `maven-assembly-plugin` to package an executable `jar-with-dependencies`
- main class is `Main`

### `sandbox/pom.xml`
- `groupId`: `hungike`
- `artifactId`: `sandbox`

---

## 4) Main module (`tag`) architecture

### 4.1 Core board model: `tag.Data`
`Data` owns board state:
- dimensions (`width`, `height`)
- `Grid[][] board`
- nested domain types: `Point`, `Grid`, `Color`

Core operations:
- `createBoard()`
- `getGrid(x,y)` with null-on-out-of-bounds behavior
- `setValue(x,y,color)`

> Caveat: `createBoard()` inner loop uses `this.board.length` as boundary; this is effectively width-based and is safer only when board is square.

### 4.2 Read-only game context: `tag.Viewer`
`Viewer` exposes game state to players without handing out the controller directly:
- board accessors
- current turn color
- game metadata (`GameInfo.round`, `GameInfo.maximusRound`)

### 4.3 Player contract: `tag.Player`
Abstract extension point:
- required: `play(Viewer v)`
- optional post-move hooks: `update(...)`

### 4.4 Rule controller: `tag.Controller`
`Controller` enforces legal moves and clear/capture logic:
- validate move position and emptiness
- place stone
- recursively detect closed regions (`tryToCollectClosedSet`)
- clear captured points (`clean`)
- score by color (`getScore`)

### 4.5 Runtime orchestrator: `tag.Host`
`Host` composes:
- `Data`
- `Viewer`
- `Controller`
- two players (`BLACK`/`WHITE`)

`run()` loop responsibilities:
1. reset state
2. alternate turns
3. request move from current player
4. apply rule logic
5. push `update(...)` notifications to players
6. terminate on manual flag or max-round bound

### 4.6 Entry point: `Main.java`
Startup defaults:
- board size `15x15`
- `maximusRound = 100`
- UI window creation via `MainFrame`
- two dropdown player lists (`getPlayerList1`, `getPlayerList2`)

Referenced player implementations:
- `GUIPlayer`
- `GameTreePlayer`
- `RandomPlayer`
- `SimplePlayer` (uses `training_ANN.nn`)
- `CNNPlayer` (uses `training_CNN.nn`)

---

## 5) `tag/player` folder detailed overview (requested)

This folder contains concrete strategy implementations and one search-based submodule.

### 5.1 `RandomPlayer`
- deterministic pseudo-random move picker (seed advanced each turn)
- repeatedly samples random `(x,y)` until an empty cell is found
- baseline strategy for comparison/testing

### 5.2 `SimplePlayer`
- feed-forward NN strategy (`MultiLayerPerceptron(10*10*2, 5, 10*10)`)
- input encoding maps board into a numeric vector
- output is interpreted as per-position score; highest valid empty cell is chosen
- can load pretrained weights from `.nn` file through `getNetwork().createFromFile(...)`

### 5.3 `CNNPlayer`
- subclass of `SimplePlayer`
- overrides network creation with a convolutional architecture:
  - input dimensions `(10,10)` with 2 maps
  - two convolution layers (`3x3` kernel)
  - one fully connected output layer
- still uses `SimplePlayer` move-decoding pipeline

### 5.4 `GUIPlayer`
- human/UI-driven player
- delegates move acquisition to `MainFrame.getPoint()`
- validates clicked point is legal (in-bounds + empty)
- pushes visual updates (`clean`, board repaint, round text)

### 5.5 `player/gametree` subpackage
Implements search player and supporting in-memory board simulation.

#### `GameTreePlayer`
- alpha-beta style negamax search
- tunables:
  - `DEPTH` (default 8)
  - `WIDTH` (default 11 candidate moves per layer)
- candidate move ordering comes from `MemBoard.getPriorityPoint(...)`

#### `MemBoard`
- contains mutable simulation board (`Data`) + `Controller`
- supports:
  - `copyData(Viewer)` to clone current board
  - `activate(x,y,color)` to apply move and record reversible actions
  - `rollback()` to undo move/captures from stack history
- computes heuristic priority for empty points based on neighbor composition

#### `PointFixedPriorityQueue`
- fixed-size sorted candidate container
- keeps top-N move candidates by priority
- used to bound branching factor (`WIDTH`) in search

### 5.6 Practical note for LLM modifications in `tag/player`
When editing this area, first decide strategy family:
- random baseline (`RandomPlayer`)
- NN scoring (`SimplePlayer` / `CNNPlayer`)
- search (`GameTreePlayer` + `MemBoard`)
- human I/O (`GUIPlayer`)

Then validate compatibility with board size assumptions:
- NN players are currently hardcoded around `10x10` tensor/vector layout.

---

## 6) Other `tag` subpackages

### `tag/exception`
Custom checked exceptions used by controller/game flow:
- `TAGException`
- `OutOfBoardException`
- `OperationProhibitedException`
- `CannotFindClosedSetException`

### `tag/ui`
Swing UI components:
- `MainFrame` (top-level frame + controls)
- `BoardPanel` (board rendering/input)

### `tag/trainning`
- `Training1.java` exists as training-related utility code.
- folder name is `trainning` (double `n`) in current source.

---

## 7) Sandbox module (`sandbox`) architecture

### 7.1 `sandbox/test.java`
- defines synthetic target function `x1^3 + x2^2 + x1`
- includes two training paths:
  - backpropagation over generated dataset
  - GA-based weight optimization via Jenetics
- evaluates and prints prediction error

### 7.2 `sandbox/test2.java`
- small isolated demo for creating/running a `ConvolutionalNetwork`

---

## 8) Runtime assets / artifacts

In `tag/`:
- `training_ANN.nn`: pretrained model for `SimplePlayer`
- `training_CNN.nn`: pretrained model for `CNNPlayer`
- `tag-0.0.1-SNAPSHOT-jar-with-dependencies.jar`: fat jar build artifact

---

## 9) Suggested mental model for future LLM edits

1. **Engine layer**: `Data`, `Viewer`, `Controller`, `Host`, `Player`.
2. **Strategy/UI layer**: `tag/player/*`, `tag/player/gametree/*`, `tag/ui/*`.
3. **Support layer**: `tag/exception/*`, `tag/trainning/*`, pretrained `.nn` assets.
4. **Experimental layer**: `sandbox/*`.

Recommended edit workflow:
1. identify target layer
2. confirm board-size assumptions
3. patch minimal files
4. run module-local Maven checks when network/plugin resolution is available

---

## 10) Quick commands

From repo root:

```bash
# module-level tests
mvn -q -f tag/pom.xml test
mvn -q -f sandbox/pom.xml test

# package runnable fat jar for main project
mvn -q -f tag/pom.xml package
```

---

## 11) Known code caveats

- `Data#createBoard()` uses width-like boundary in inner loop.
- naming typos are present but consistent (`getHeigth`, `maximusRound`, `trainning`, `taring*`).
- some players assume fixed board encoding shapes (not fully dynamic with arbitrary board sizes).

