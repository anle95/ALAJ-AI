#include "Reversi.h"
//#include "ReversiMain.h"
//#include "ReversiLogic.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

struct GameNode {
     struct GameNode *parent, *children;
     struct GameState *state;
};

void findCompMove (struct GameState *Game, int depth, char *outputMove) {
    int maxDepth = 5;
    if (depth > maxDepth) {
        return;
    }
    int count = 0;
    int best = -10000;
    char *move = "i9";
    struct GameNode children[60];
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (viableMove(Game, i, j)) {
//                struct GameNode *child = children+count;
                struct GameNode *child;
            printf("1");
                child->state = (struct GameState *)malloc(15);
            printf("2");
                memcpy(child->state, Game, sizeof(*Game));
                printf("%s",child->state->board[3][3]);
//                child->state = *Game;
//                child->max = 1 - Game->max;
                child->state->currentP = Game->currentO;
                child->state->currentO = Game->currentP;
//                child->parent-> = Game;
//                child->state->board
//                child->state->board = Game->board;
                memcpy(child->state->board, Game->board, sizeof(Game->board));
                update(child->state, i, j);
                int eval = evalFunc(child->state);
                if (eval > best) {
                    *move = '1'+i;
                    *(move+1) = 'a'+j;
                    best = eval;
                }
            }
        }
    }
    strcpy(outputMove, move);
}
int evalFunc(struct GameState *Game) {
    return Game->black - Game->white;
}

