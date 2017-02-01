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

void getAvailableMoves(struct GameState Game, struct Move output[32]) {
    int count = 0;
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (viableMove(&Game, i, j)) {
                output[count].x = i;
                output[count].y = j;
                count++;
            }
        }
    }
    output[count].x = output[count].y = -1;
}

int evaluate(struct GameState Game, struct Move *move) {
    char m[3];
    moveToString(*move, m);
    play(&Game, m);
    return (Game.black - Game.white);
}

void findCompMove (struct GameState Game, int depth, char *outputMove) {
    int maxDepth = 5;
    if (depth > maxDepth) {
        return;
    }
    int count = 0;
    int best = -10000;
    char move[3] = "i9\0";
    struct GameNode children[60];
    struct Move available[32];
    getAvailableMoves(Game, available);
    // for (int i = 0; i < 32; i++) {
    //     if(mbuffer[i].x > 7) {
    //         break;
    //     }
    //     moveToString(mbuffer[i], move);
    //     printf("%s\n", move);
    // }

    for (int i = 0; available[i].x != -1; i++) {
        int eval = evaluate(Game, &available[i]);

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
