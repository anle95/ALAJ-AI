#include "Reversi.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

struct GameNode {
     struct GameNode *parent, *children;
     struct GameState state[8][8];
};

struct Move {
    int x, y;
};

void moveToString(struct Move m, char *output) {
    char str[3];
    str[0] = m.x + '1';
    str[1] = m.y + 'a';
    str[2] = '\0';
    strcpy(output, str);
}

void getAvailableMoves(struct GameState *Game, struct Move output[32]) {
    int count = 0;
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (viableMove(Game, i, j)) {
                output[count].x = i;
                output[count].y = j;
                count++;
            }
        }
    }
    output[count].x = output[count].y = -1;
}

int evaluate(struct GameState *Game) {
    if (Game->currentP == Black)
        return (Game->black - Game->white);
    else return (Game->white - Game->black);
}

int recFind (struct GameState Game, struct Move m, int depth) {
    char move[3];
    moveToString(m, move);
    play(&Game, move);

    int maxDepth = 4;
    if (depth > maxDepth)
        return evaluate(&Game);

    int best = -10000;
    struct Move available[32];
    getAvailableMoves(&Game, available);
    for (int i = 0; available[i].x != -1; i++) {
        int value = recFind(Game, available[i], depth+1);
        if(value > best)
            best = value;
    }
    return best;
}

void findCompMove (struct GameState Game, char *outputMove) {
    int best = -10000;
    char move[3] = "i9\0";
    struct GameNode children[60];
    struct Move available[32];
    getAvailableMoves(&Game, available);
    for (int i = 0; available[i].x != -1; i++) {
        int eval = recFind(Game, available[i], 0);

        // char tmp[3];
        // moveToString(available[i],tmp);
        // printf("  %s: %i\n", tmp, eval);

        if(eval > best) {
            moveToString(available[i], move);
            best = eval;
        }
    }
    strcpy(outputMove, move);
}
