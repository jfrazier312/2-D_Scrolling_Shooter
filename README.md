README
Jordan Frazier (jrf30)

A basic javaFX game. Design.txt located in Compsci308_Project1_Game/src/game.

TODO: 
TODO: Collision with enemy ship
TODO: Implement Explosion.java
TODO: Create missile launch animation for boss scene
TODO: Move all key inputs to new class?
TODO: Create static class in GameView to keep track of GameState (instead of using so many instance variables/fields)
TODO: General clean up of comments, print statementds

Issues: 
Too many instance variables
Gameview class is too large
More efficient method to track win/lost besides timeline boolean checker
If I am using Main.java to control scene changes, is there a way to implement GameView.initGame()/animateGame() methods such that
they can take in local (GameView.java) variables and still be able to be called by the Main.java class? (which would reduce the amount
of instance variables I am using in GameView.java)

Cheat Keys:
'd' - infinite lives 
'f' - infinite ammo
's' - skip to boss battle from shooting scene
'a' - automatically lose all hit points in shooting scene
'b' - automatically input missile launch sequences in boss battle


Images from:
http://www.desktopwallpaperhd.net/wallpapers/18/5/space-wallpaper-earth-background-cool-mania-186853.jpg
filmconnoisseur.blogspot.com
millionthvector.blogspot.com