##Introduction
Our reworked app game introduces new games, while carrying over great features and functionality.
Our new games include matching tiles, snake game, and the knight's tour game. *Matching tiles* implements
functionality such as different board difficulties, user manager, scoreboard, and autosave. The *snake
game* implements functionality such as different board difficulties, user manager, scoreboard, and undo.
 Lastly, the *knight's tour* implements different board difficulties, user manager, and scoreboard.
Additionally, now the scoreboard can be accessed from the the main menu. Players who are not logged
in can view global high scores while logged in players can view personal (denoted as local) high scores.


## Layout Design
The bulk of the game is located at *Phase2\GameCenter\app\src\main\java\group_0661\gamecenter*, where each
of the subdirectory has the following functionality:
    * gameSystem: sets up the game board of varying sizes and contains the logic of movement
      control including undo and makemove, specifically for slidingTiles.
    * gestures: detects tapping action and sets up the gridview.
    * scoreboard: generates the scoreboard based on global ranking and personal ranking.
    * user: creates a new user and manages the existing users.
    * knightsTour, snake, and matchingTiles contains the game logic for the respective games.
    * Other files are responsible for layout and user interaction


## Game Setup
press *File* on the navigation bar on top, select *Import Project* under *New* and
choose the folder *gamecentre* in the list of directories. This will set up the gradle.
Now to run the game, select *edit configurations* in dropdown tab beside the run button on the
top navigation bar. Under *launch options*, select *specified activity* under *launch*.
Press the ellipsis on the right of *activity* and select *MainMenuActivity*.
Now the game should run properly.

##Game Manual

#Heads-up:
    Anonymous players will be disabled from most functional component of the game including
    save and leaderboard. To enjoy full access, make sure that you are signed in.
    Toggle between games by clicking the game title!

On the menu page, the player can:
    * Choose which game they wish to play. Switch between our 4 games by clicking the game title.
    * Start a new game with/without an account, which takes you to the game set up page.
    * Load a previously saved game under an account (must login to load).
    * If not logged in, go to account register/login page.
    * If logged in, log out.

On the sign in/sign up page, the player can:
    * Sign up with a unique username and password.
    * Sign in.

On the sliding tiles set up page, the player has the option to:
    * Choose a board size. (mandatory)
    * Set the number of undo, by default is 3. Check the checkbox beside it to undo unlimited times.
     The number of undo refreshes after each new move in this game.
    * Add a background to the board. (Optional)
    * Start the game when ready.
On the sliding tiles game page, the player can:
    * Press the undo on the upper-right corner to revert the last step,
      up to the number of undo selected in game setup.
    * Press the save button beside it to save game, requires login first.

On the snake set up page, the player has the option to:
    * Choose a board size. (mandatory)
    * Start the game when ready.
On the snake game page, the player can:
    * Press the undo on the upper-right corner to revert the last step,
      up to the number of undo selected in game setup.
    (* Press the save button beside it to save game, requires login first.)

On the knight's tour set up page, the player has the option to:
    * Choose a board size. (mandatory)
    * Start the game when ready.
On the knight's tour game page, the player can:
    * Press the undo on the upper-right corner to revert the last step,
      up to the number of steps taken.
    * Press the save button beside it to save game, requires login first.
    * Reach every tile in the board to win!

On the matching tiles set up page, the player has the option to:
    * Choose a board size. (mandatory)
    * Start the game when ready.
On the matching tiles game page, the player can:
    * Press the save button beside it to save game, requires login first.
    * Match all the tiles to win!

On the score page, the player can:
    * Check their local/global score by toggling the button on upper-left corner
    * Select the board size by selecting one option from the dropdown menu
    * **Scores will only display for logged in users**