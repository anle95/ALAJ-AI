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
    // printf("Enter your color (Black or White): \n");
    // char pChar[6];
    // while (scanf("%s", pChar) != 1 ||
    //     (strcmp(pChar, "Black") != 0 && strcmp(pChar, "White") != 0) ) {
    //         printf("Invalid choice, please choose Black or White (mind the capital letters): \n");
    // }
    //
    // Player p = (strcmp(pChar, "Black") == 0) ? Black : White;
    Player p = White;
    char computerMove[3];
    printBoard(&Game);
    while (1) {
        if (!anyViableMove(&Game)) break;

        if (Game.currentP == White) {
            if (scanf("%s", input) != 1)
                break;
            play(&Game, input);
            // minimax(&Game, 2, computerMove);
            // printf("ComputerW plays -%s-\n", computerMove);
            // play(&Game, computerMove);
        } else {
            minimax(&Game, 2, computerMove);
            printf("ComputerB plays -%s-\n", computerMove);
            play(&Game, computerMove);
        }
        printBoard(&Game);
    }
    char *winner = (Game.black > Game.white) ? "Black" : "White";
    printf("%s wins!\n", winner);
    scanf("%s", input);
    return 0;
}
