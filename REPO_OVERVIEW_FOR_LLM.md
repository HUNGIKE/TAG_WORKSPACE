# TAG_WORKSPACE Repository Overview (for LLM-assisted changes)

## 1) Repository purpose (high-level)
This repository is a Java workspace with two Maven modules:

- `tag/`: the main game project (a board game engine + player framework, with AI-related dependencies).
- `sandbox/`: experimental code for neural networks / genetic algorithm training experiments.

There is also a root-level `README.md` with a minimal description.

---

## 2) Top-level layout

```text
TAG_WORKSPACE/
├─ README.md
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
│        └─ Viewer.java
└─ sandbox/
   ├─ pom.xml
   └─ src/main/java/sandbox/
      ├─ test.java
      └─ test2.java
```

---

## 3) Build system and dependencies

Both subprojects are Maven projects.

### `tag/pom.xml`
- `groupId`: `hungike`
- `artifactId`: `tag`
- dependencies:
  - `org.beykery:neuroph:2.92`
  - `io.jenetics:jenetics:3.8.0`
- uses `maven-assembly-plugin` to produce an executable **jar-with-dependencies** with main class `Main`.

### `sandbox/pom.xml`
- `groupId`: `hungike`
- `artifactId`: `sandbox`
- dependencies are the same two libraries (`neuroph`, `jenetics`).

---

## 4) Main module (`tag`) architecture

## 4.1 Core domain model: `tag.Data`
`Data` stores the board state.

- `width`, `height`
- `Grid[][] board`
- nested types:
  - `Point { int x, y }`
  - `Grid { Color color }`
  - `enum Color { BLACK, WHITE }` with `rivalColor()` helper

Key methods:
- `createBoard()`: initialize board with empty `Grid` objects.
- `getGrid(x, y)`: returns grid or `null` when out-of-bounds.
- `setValue(x, y, color)`: set piece color in a cell.

> Note: `createBoard()` initializes `j` using `this.board.length` instead of `this.height`; this works for square boards but is potentially unsafe for rectangular boards.

## 4.2 Read-only game view: `tag.Viewer`
`Viewer` wraps a `Data` instance and exposes information to players:

- current player color (`color`)
- game metadata (`GameInfo`), including:
  - `round`
  - `maximusRound` (max rounds)

Typical usage:
- players receive `Viewer` in `play(Viewer v)` and choose next move based on visible state.

## 4.3 Player abstraction: `tag.Player`
Abstract base class:

- required: `Data.Point play(Viewer v)`
- optional hooks:
  - `update(Viewer v, List<Data.Point> closeSet)`
  - `update(Viewer v)`

This design allows human, random, rule-based, and NN-based player implementations.

## 4.4 Rule enforcement: `tag.Controller`
`Controller` owns game-rule logic.

Responsibilities:
- board reset (`reset`)
- score calculation (`getScore(Color)`)
- placing a move (`setValue(x, y, color)`)
- capture-like closure detection:
  - `tryToCollectClosedSet(...)`
  - `clean(List<Point>)` to clear closed regions

Behavior summary:
1. Validate move is in-bounds and on empty cell.
2. Place piece.
3. Check neighbors; if opposite-color regions are fully enclosed (no path to empty), collect/clean them.
4. If none captured, also test own region closure.

The algorithm is DFS-like recursion over 4-neighbor connectivity.

## 4.5 Game lifecycle / orchestration: `tag.Host`
`Host` wires together:
- `Data`
- `Viewer`
- `Controller`
- two `Player` instances (BLACK/WHITE)

Main loop (`run()`):
1. Reset board and round.
2. Alternate players.
3. Ask current player for move via `play(viewer)`.
4. Apply move through controller.
5. Notify all players with `update(...)`.
6. Increase round and terminate at `maximusRound` (if set).

`resetGame()` flags termination.

## 4.6 Entry point: `Main.java`
Current startup flow:
- Create `Host(15,15)`.
- Set max rounds to 100.
- Create `MainFrame` UI and connect host.
- Build 2 selectable player lists via `getPlayerList1/2`.

Player types referenced in `Main.java`:
- `GUIPlayer`
- `GameTreePlayer`
- `RandomPlayer`
- `SimplePlayer` (loads `training_ANN.nn`)
- `CNNPlayer` (loads `training_CNN.nn`)

### Important repository gap
In this checkout, source files for the following packages are **not present**:
- `tag.player.*`
- `tag.player.gametree.*`
- `tag.ui.*`
- `tag.exception.*`

`Main.java`, `Host.java`, and `Controller.java` import these packages, so a clean source-only build from current files may fail unless those classes are provided elsewhere (e.g., in prebuilt jar, another branch, or omitted files).

---

## 5) Sandbox module (`sandbox`) architecture

## 5.1 `sandbox/test.java`
Experiment for NN fitting a synthetic function:

- estimated target function: `f(x1,x2) = x1^3 + x2^2 + x1`
- supports two training strategies:
  - backpropagation (`taringByBP`) on large random dataset
  - genetic algorithm (`taringByGA`) using Jenetics and network weights as chromosome
- uses Neuroph `MultiLayerPerceptron`
- `main` currently randomizes weights, trains by BP, then prints estimation error samples.

## 5.2 `sandbox/test2.java`
Small experiment constructing a `ConvolutionalNetwork` with builder API, assigning weights/input, and printing outputs.

---

## 6) Runtime assets / artifacts

In `tag/`:
- `training_ANN.nn`: serialized NN model used by `SimplePlayer`.
- `training_CNN.nn`: serialized NN model used by `CNNPlayer`.
- `tag-0.0.1-SNAPSHOT-jar-with-dependencies.jar`: prebuilt fat jar.

These files suggest the project has been run/trained before, even if some source code is currently missing from this checkout.

---

## 7) Suggested mental model for future LLM edits

When making changes, treat repo as:

1. **Core board engine layer** (`Data`, `Viewer`, `Controller`, `Host`, `Player`) — mostly complete in source.
2. **Strategy/UI/extensions layer** (`tag.player.*`, `tag.ui.*`, `tag.exception.*`) — referenced but incomplete/missing in current tree.
3. **Research playground** (`sandbox`) — separate experiments, not core runtime loop.

Practical strategy:
- If task touches core rules: work under `tag/src/main/java/tag/`.
- If task touches UI/player implementations: verify missing sources first before editing.
- Use `tag` and `sandbox` as independent Maven modules.

---

## 8) Quick commands (for future maintainers)

From repo root:

```bash
# Compile main module (may fail if missing source packages are required)
cd tag && mvn -q test

# Compile sandbox experiments
cd sandbox && mvn -q test
```

If compile fails due to missing classes, confirm whether the missing sources exist in another branch/repo or are expected to come from external binaries.

---

## 9) Known issues / caveats summary

- Missing source packages referenced by imports (`tag.player`, `tag.ui`, `tag.exception`).
- `Data#createBoard` inner loop appears width-based, which is only safe for square boards.
- Some class/method naming typos exist (`getHeigth`, `maximusRound`, `taringByBP/GA`), but these are consistent in current code.

