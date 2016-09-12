## DESIGN.md

High Level Design Goals:
 - This game is designed to have a main start screen that controls when to switch scenes. 
There are four main scenes: the start screen, the scrolling shooter, the boss scene, and highscores scene.
A high level interface, GameWorld.java, will be implemented by many classes in order to define some basic constants, such as 
scene dimensions. A sprite class will be a superclass for both the player's ship and enemy ships. 

New Features:
 - If you wanted to add a new level/scene to the game, you would create a new class (newFeature.java). According to my design now,
which I would like to optimize somehow in the future, you would need to add a boolean checker within a timeline in Main.java.
This timeline would be in a new method in Main.java, and the class would be instantiated in Main.java as well. If newFeature.java
should create a new scene and be switched to, then it's boolean should switch from false to true, which should trigger the 
timeline in Main.java to execute stage.setScene(newFeature.getScene());

Major Design Choices:
 - The major design choice that I think is the bottleneck, or would be the future bottleneck, is the necessity to have a 
 timeline checking a boolean every frame. There should be a way to switch scenes without passing the stage around and 
also the boolean checker + timeline. 

Ambiguities in Project Functionality:
 - ???
