# NorseTown 
Voxel Game built in Java with lwjgl.
It is a Town Builder/Simulator themed around Viking Age Culture & Norse Mythology.

Currently has no gameplay. Engine works with a chunk based data, where each block is stored as a static object and associated with an integer id. Last things I implemented were water shaders (reflections with dydv displacement to easily simulate ripples), and dynamic block modeling using cuboids.

Next things I would implement were I to return to this project would be moving block model definitions to be loaded from resource files rather than hard coded; octtree collision detection to support trees and (eventual) ships being seperate objects from chunks; and creating a proper tasking engine for pawn ai.

This game requires Java 8 and OpenGL version of at least 3.3
