# EightPool  
A Java-based billiards (8-ball/pool) simulation and gameplay project with advanced AI capabilities.

## 🚀 Overview  
EightPool is a modern pool-game engine built in Java, designed not only to provide realistic physics and gameplay, but also to include smart AI opponents and intelligent shot-planning. Whether you’re building a pool game, simulation platform, or simply exploring billiard AI, this repository gives you a solid foundation and flexible architecture.

## 🧠 AI Features & Capabilities  
One of the key differentiators of EightPool is its AI system — here’s what makes it special:

- **Trajectory-analysis & decision making**: The AI assesses the table state, computes viable shot trajectories (including direct and bank shots), and selects an optimal choice based on game state, risk, and reward.  
- **Strategic planning horizon**: Rather than simply aiming at the next ball, the AI can evaluate *positioning* of the cue ball and plan for multi-shot sequences, increasing its effectiveness at higher skill levels.  
- **Difficulty scaling**: Want a beginner-friendly opponent? The AI introduces aiming and force inaccuracy, simplifies decision depth, and occasionally opts for safety shots. Want a challenging opponent? It computes deeper look-ahead, picks stronger shots, and plays more aggressively.  
- **“Human-like” behaviour**: To avoid robotic perfection, the AI can incorporate subtle errors (slight angle offsets, imperfect force), hesitations, and tactical shifts, giving players a more natural and engaging opponent.  
- **Physics-aware planning**: The system is built with a realistic billiard physics engine in mind — accounting for bounce, spin, collisions – and the AI uses that to predict outcomes more accurately and make smarter decisions.  
- **Extensibility**: You can plug in your own AI modules (e.g., Monte Carlo simulation, reinforcement learning, rule-based heuristics) into the architecture. EightPool is designed to support experimentation and expansion of the AI layer.

## 🎮 Key Features  
Beyond the AI, EightPool offers:  
- Realistic 2D/3D billiard table simulation (including collisions, cushion bounce, ball‐ball interaction).  
- Configurable game rules (eight-ball, nine-ball, one-pocket, etc.).  
- Modular architecture: physics engine, game logic, AI logic are decoupled to support customization.  
- Java & Gradle build system: easy to compile, test and extend.  
- Sample resources (textures, table models, cues) included to get you up and running quickly.

## 📦 Getting Started  
### Prerequisites  
- Java 11 + (or your preferred supported version)  
- Gradle (or use the included `gradlew` wrapper)  
- A suitable IDE (IntelliJ, Eclipse) or CLI tools  

### Building & Running  
1. Clone the repository  
   ```bash
   git clone https://github.com/iKozay/EightPool.git  
   cd EightPool  
   ```

2. Build the project

   ```bash
   ./gradlew build  
   ```
3. Run the game / simulation

   ```bash
   ./gradlew run  
   ```

   (Depending on your setup, you may launch the GUI or CLI version.)

### Configuring the AI

Inside the project you’ll find a directory (e.g., `src/main/java/ai`) where the AI logic lives. You can modify key parameters such as:

* search depth / look-ahead horizon
* aim/force error rate (for difficulty adjustment)
* risk tolerance (safe vs aggressive shots)
* selection criteria (score highest probability of potting vs positional advantage)

## 🧩 Architecture at a Glance

```
├── physics/           # ball dynamics, collisions, table model  
├── game/              # rules, turn logic, scoring  
├── ai/                # opponent AI, shot selection logic  
├── ui/                # (optional) graphical / console interface  
└── resources/         # textures, models, table layouts  
```

The AI module interacts with the physics engine to simulate candidate shots, evaluates outcomes, then selects the best one per configured strategy.

## 🧪 Example Use-Case

1. Player breaks the rack.
2. The AI inspects the remaining balls and possible pockets.
3. For each viable ball-pocket pair it simulates trajectories (including cushion bounce) and evaluates outcomes (potting success, cue-ball position for next shot).
4. It selects the shot with best expected outcome according to its strategy (e.g., maximize potting probability vs minimize opponent’s advantage).
5. AI executes the shot, applies realistic aim/force with slight variation (based on difficulty).
6. Next turn continues until game concludes.

## 💡 Why This Project?

* If you’re building a billiards game, EightPool gives you both physics + intelligent opponent logic out-of-the-box.
* If you’re exploring game AI, it offers a clear corridor to experiment with realism, difficulty scaling, and strategic depth.
* If you’re a hobbyist or student, this is a fun domain to combine geometry, physics, heuristic search and AI behaviour in one concrete project.

## ✅ Contribution & Roadmap

Interested in contributing? Some ideas:

* Add support for **spin/English** on cue‐ball and enable AI to use spin strategically.
* Implement **Monte Carlo shot simulation** for deeper planning and richer AI behaviour.
* Create **dynamic difficulty adaptation**: AI adjusts in real-time based on player performance.
* Build **multiplayer mode** (human vs human, human vs AI) and networked gameplay.
* Improve UI/UX: add 3D graphics, cue stick animations, fancy effects.
* Integrate **machine learning**: e.g., train neural-net to select shots based on table state.

## 📝 License

[Specify your license here, e.g., MIT, Apache 2.0, GPL-3.0 — whichever you choose].
By contributing you agree to abide by the license terms.

## 📬 Contact / Support

For issues, feature requests, or general discussion, please open an Issue in this GitHub repository.
Thanks for checking out EightPool!
