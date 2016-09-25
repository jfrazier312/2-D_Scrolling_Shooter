README <br>
Jordan Frazier 

Initialize <br> 
Ensure your IDE supports JavaFX. Clone repo into local directory and run Main.java. 

Resources:
- http://docs.oracle.com/javafx/2/get_started/jfxpub-get_started.htm
- http://docs.oracle.com/javase/8/javase-clienttechnologies.htm
- http://stackoverflow.com/questions/116519/best-resources-for-learning-javafx

Images:
- http://www.desktopwallpaperhd.net/wallpapers/18/5/space-wallpaper-earth-background-cool-mania-186853.jpg
- filmconnoisseur.blogspot.com
- millionthvector.blogspot.com

Main.java will start the main screen.

TODO: 
- TODO: Collision with enemy ship
- TODO: Implement Explosion.java
- TODO: Create missile launch animation for boss scene
- TODO: Move all key inputs to new class? (CheatCodes renamed to KeyInputs and implemented)
- TODO: Create static class in GameView to keep track of GameState (instead of using so many instance variables/fields)
- TODO: General clean up of comments, print statements
- TODO: Implement enemy moving algorithm instead of random movements. It would be nice to create a cubic curve to have them circle the screen. 

Bugs:
- In BossBattle, if user presses input keys when clicking 'Next' through arrow key prompts, game interprets that as 
incorrect sequence and will cause game over button to appear.
  - Solution is to not allow for inputs during this dialogue period, or only allow mouse
    click to further progress. 

Design Issues: 
- Too many instance variables
- Gameview class is too large - could use static class to control game state instead of multiple instance variables
- More efficient method to track win/lost besides timeline boolean checker
- If I am using Main.java to control scene changes, is there a way to implement public GameView.initGame()/animateGame() 
methods such that they can take in local (GameView.java) variables and still be able to be called by the Main.java class? 
(which would reduce the amount of instance variables I am using in GameView.java)

Cheat Keys:
- 'd' - infinite lives 
- 'f' - infinite ammo
- 's' - skip to boss battle from shooting scene
- 'a' - automatically lose all hit points in shooting scene
- 'b' - automatically input missile launch sequences in boss battle

Features:
- This game features a startup screen, with options to look at the rules, cheat codes, or start the game.
The main game scene (GameView.java) features a Galaga-style scrolling shooter. After a timer ends,
you are transported to the Boss scene (BossBattle.java). This mode will test your memory, as
you must input several, increasingly longer, sequences of arrow key inputs in order to fire the 
missile and defeat the boss. If you win, then you will be taken to a high scores scene (HighScoreView.java).
This scene features a textfield to input your initials, and a list that will keep the top three scores
and sort them by score (the number of enemy ships you destroyed in GameView.java scene). From this screen,
you can enter your score, then hit retry to go back to the start screen. If you do not end the program,
the high scores will be saved and you can try to beat your last score. 
