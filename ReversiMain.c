#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Reversi.h"

//kompileringskommando: gcc -o reversi ReversiMain.c ReversiLogic.c ReversiAI.c
int main(void) {
    //set up initial GameState
    struct GameState Game;
    resetGame(&Game);

    char input[2];
    printf("Enter your color (Black or White): \n");
    char pChar[6];
    while (scanf("%s", pChar) != 1 ||
        (strcmp(pChar, "Black") != 0 && strcmp(pChar, "White") != 0) ) {
            printf("Invalid choice, please choose Black or White (mind the capital letters): \n");
    }

    Player p = (strcmp(pChar, "Black") == 0) ? Black : White;
    char moveBuffer[3];
    printBoard(&Game);
    while (1) {
        if (!anyViableMove(&Game)) break;

        if (Game.currentP == p) {
            // minimax(&Game, 2, moveBuffer);
            // printf("I suggest -%s-\n", moveBuffer);
            if (scanf("%s", input) != 1)
                break;
            play(&Game, input);
            // printf("Computer plays -%s-\n", moveBuffer);
            // play(&Game, moveBuffer);
        } else {
            minimax(&Game, 7, moveBuffer);
            printf("Computer plays -%s-\n", moveBuffer);
            play(&Game, moveBuffer);
        }
        printBoard(&Game);
    }
    char *winner = (Game.black > Game.white) ? "Black" : "White";
    printf("%s wins!\n", winner);
    scanf("%s", input);
    return 0;
}
