# Villager Pickup

A Minecraft Fabric mod for Minecraft 1.21.4 that allows players to pick up villagers and place them back down with all their data intact.

## Features

- Pick up villagers by shift-right-clicking on them
- Villagers are stored in your inventory as items with custom tooltips showing profession and level
- Place villagers back in the world by right-clicking on a block
- All villager data is preserved (trades, profession, experience, inventory, etc.)
- Sound effects when picking up or placing down villagers
- Enchantment glint on items containing villagers
- Detailed tooltips showing villager information

## Usage

1. Make sure your hand is empty
2. Sneak (shift) and right-click on a villager to pick it up
3. The villager will be stored in your inventory as an item
4. Right-click on any block to place the villager back in the world
5. The villager will maintain all of its original data and trades

## Visual and Audio Feedback

- Sound effect when picking up a villager
- Sound effect when placing down a villager
- Green success messages in chat
- Red error messages if something goes wrong
- Custom tooltip showing villager profession and level

## Requirements

- Minecraft 1.21.4
- Fabric Loader 0.16.13
- Fabric API
- Cardinal Components API (included)

## Installation

1. Install Fabric Loader for Minecraft 1.21.4
2. Download the latest version of the mod from the releases page
3. Download the required version of Fabric API
4. Place both jar files in your mods folder
5. Launch Minecraft with the Fabric profile

## Development

This mod was built using:
- Java 21
- Gradle
- Fabric API
- Cardinal Components API for storing villager data

### Building from Source

1. Clone the repository
2. Run `./gradlew build`
3. Find the built jar in the `build/libs` directory

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Credits

- Inspired by various villager management mods including [Not-As-Easy-Villagers](https://github.com/igoro00/not-as-easy-villagers)
- Uses Cardinal Components API for data storage
- Thanks to the Fabric community for their helpful documentation and examples
