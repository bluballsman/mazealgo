# mazealgo

## What is it?

This repository is meant for a maze algorithm library for use in my personal blockgame minigame, specifically for Minecraft and the upcoming
game Hytale. These mazes are created with 3 specific purposes:

1. Create a tiled maze algorithm using randomized DFS. I chose DFS because it gave me these nice, long corridors other algorithms didn't give me and because
of its ease of implementation. I am going for a tile approach instead of a node-based approach because these mazes will be built in a block game, so square tiles
translate directly to blocks. To open more paths through the maze and prevent it from being one long corridor, I randomly break walls after the DFS is done.
2. Symmetry. These mazes are meant to be built as a map for a pvp-based game, and to keep things fair I needed to make sure the maze looked identical on both sides.
3. Easy way to add structures. I am planning on adding randomized structures found throughout each maze to keep each session feeling fresh. These structures will be pre-built
by me and others in game with the goal of a large pool of possible structures that can spawn in the maze. However, these structures must follow the natural architecture of the
maze. Structures will spawn in specific places given by their structure "blueprint", which determines where exactly a structure can spawn. This will be explained later.

Because I am going with a tiled approach, odd and even coordinates in the maze are of upmost importance. When the DFS algorithm is clearing paths through the maze, it must clear
walls in twos because otherwise there would be no walls in the maze. This makes it so that I must be careful when adding any extra structures in the maze which I will cover in a
bit.

## Tiles

At its core, the maze is a 2D array of tiles. Each tile holds 2 pieces of information: whether it's ground and whether it's marked as a part of a structure. Additionally, you 
can get the "type" of the tile. The types of tiles are as follows:

1. T
2. Corner
3. Straight
4. End
5. Cross
6. Alone

These 6 tile types are useful when actually building the maze in game. The tile type is determined by the 4 immediate tiles that surround any given tile. A standalone tile,
for example, is a tile which is surrounded by tiles which do not match it, like a wall tile surrounded by ground tiles and vice versa. This tile type and its rotation is
determined by what I call the tile's "type code". The type code is a 4 bit representation of the 4 surrounding tiles, from left to right in north, east, south, west order. 
The leftmost bit is a "1" if the adjacent tile matches the current tile and a "0" otherwise. For example, a typecode of 1010 would mean that the north and south tiles matched.
This code gives you not only the type of tile, but also how many rotations from the default the tile is. This is used when actually building the maze in game. 

## Structures

A structure is an object which will be built on top of the maze after all the tiles have been generated. Structures will be used to add things like items in dead ends, randomly
generated rooms, and the tower at the center of the maze. Special structures called rooms are directly placed before the maze is actually generated. This is to ensure that the
room is not only accessible, but does not disrupt the natural order of the maze. Without this, rooms could overlap with the pre-existing maze and block the maze off. Structures
are represented by something I call a "blueprint", which is a simple representation of the types of tiles the structure represents. A "1" means a ground tile, a "0", represents
a wall tile, and an "X" represents either. An example of a blueprint for a dead end is given below:

<strong>
<p align = "center">
000 <br>
010
</p>
</strong>

In the code, the line break is represented as a period. Each structure must be rectangular, so each line of the structure must be the same as the others. When the maze is done
generating, you can add structures using the placeStructure() method, which will try to find a random place where your structure fits regardless of rotation. Once your structure
is placed, all the tiles in the structure have their structure flags set. The blueprint gets converted into a StructureSlot object, which is placed in the structure list. 

## Concluding thoughts

Overall, this is just a fun personal project that I did in my free time. I think I can optimize the code more but I don't know exactly how much, I wouldn't call it as optimal
as possible but I am not sure if drastic improvements can be made. 




