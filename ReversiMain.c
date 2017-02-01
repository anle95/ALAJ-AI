#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Reversi.h"

//kompileringskommando: gcc -o reversi ReversiMain.c ReversiLogic.c ReversiAI.c
int main(void) {
    //set up initial GameState
    struct GameState Game;
    resetGame(&Game);
    int gameOver = 0;

    char input[2];
    printf("Enter your color (Black or White): \n");
    char pChar[6];
    while (scanf("%s", pChar) != 1 ||
        (strcmp(pChar, "Black") != 0 && strcmp(pChar, "White") != 0) ) {
            printf("Invalid choice, please choose Black or White (mind the capital letters): \n");
    }

    Player p = (strcmp(pChar, "Black") == 0) ? Black : White;
    char computerMove[2];
    printBoard(&Game);
    while (1) {
        if (!anyViableMove(&Game)) {
            Game.currentO = Game.currentP;
            Game.currentP = (Game.currentP == Black) ? White : Black;
            gameOver = 1;
            continue;
        }
        if (Game.currentP == p) {
            if (scanf("%s", input) != 1)
            break;
            if(play(&Game, input)) {
                Game.currentO = Game.currentP;
                Game.currentP = (Game.currentP == Black) ? White : Black;
                gameOver = 0;
            }
        } else {
            findCompMove(&Game, 0, computerMove);
//            compMove(&Game, computerMove);
            printf("Computer plays: %s\n", computerMove);
            Game.currentO = Game.currentP;
            Game.currentP = (Game.currentP == Black) ? White : Black;
            gameOver = 0;
        }
        printBoard(&Game);
    }

    return 0;
}
