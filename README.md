# GunBound-inspired battle game

Written in February 2013, this is an improved version of my previous GunBound-inspired battle game. The change is programming language from Python to Java was most likely motivated by the desire of learning a new language. Most modifications are aimed to make it more difficult for the players to target each other.

## Improvements

- Cannons vertical position oscillates.
- Their horizontal position can be adjusted: left player press 1 (2) to go left (right) / right player press 8 (9) to go left (right).
- A tornado appears periodically (randomly could be better), its x position oscillates.
- Canon speed and angle are set through horizontal bars (a slider could be more intuitive).
- There is no energy dissipation (air resistance).
- No wind either.
- Only gravitational forces are considered.
- Life bars added.
 
## Comments

- Drawings and graphics use the Standard drawing library StdDraw.java, which may be deprecated (?).
- Brief description on how to play should be added.
- Personalized avatars and background must be added (no copyright infringement is intended by using the current figures).

## Compilation

javac main.java

## Run

java main
