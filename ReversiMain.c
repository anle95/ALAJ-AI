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
    while (!gameOver) {
        if (!anyViableMove(&Game)) {
            gameOver = 1;
            continue;
        }

        if (Game.currentP == p) {
            if (scanf("%s", input) != 1)
            break;
            if(play(&Game, input)) {
                gameOver = 0;
            }
        } else {
            findCompMove(Game, computerMove);
            play(&Game, computerMove);
            printf("Computer plays: %s\n", computerMove);
            gameOver = 0;
        }
        printBoard(&Game);
    }

    return 0;
}
