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

void moveToString(struct Move m, char *output);
void getAvailableMoves(struct GameState *Game, struct Move output[32]);
int evaluate(struct GameState *Game);
int maxValue(struct GameState Game, struct Move *m, int alpha, int beta, int depth);
int minValue(struct GameState Game, struct Move *m, int alpha, int beta, int depth);

int MAX_DEPTH = 15;

void printdepth(int d) {
    for(int i = 0; i < d; i++) printf("  ");
}

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
    return (Game->black - Game->white);
}

void minimax(struct GameState *Game, int depth, char *output) {
    MAX_DEPTH = depth;
    struct Move actions[32];
    int bestMoveIndex = 0;
    int best = -100;
    getAvailableMoves(Game, actions);
    for (int i = 0; actions[i].x != -1; i++) {
        int min = (Game->currentP == Black) ?
            minValue(*Game, &actions[i], -100, -100, 1):
            maxValue(*Game, &actions[i], -100, -100, 1);
        char tmp[3];
        moveToString(actions[i],tmp);
        printf("%s: %i\n", tmp, min);
        if (min > best) {
            best = min;
            bestMoveIndex = i;
        }
    }

    moveToString(actions[bestMoveIndex], output);
}

int maxValue(struct GameState Game, struct Move *m, int alpha, int beta, int depth) {
    char move[3];
    moveToString(*m, move);
    play(&Game, move);

    if(m->x == -1 || depth > MAX_DEPTH) return evaluate(&Game);

    int value = -100;
    struct Move actions[32];
    getAvailableMoves(&Game, actions);
    for (int i = 0; actions[i].x != -1; i++) {
        int min = minValue(Game, &actions[i], alpha, beta, depth+1);
        char tmp[3];
        moveToString(actions[i],tmp);
        printdepth(depth);
        printf("    %s: %i\n", tmp, min);
        value = (value > min) ? value : min;
        if(value >= beta) return value;
        alpha = (alpha > value) ? alpha : value;
    }
    return value;
}

int minValue(struct GameState Game, struct Move *m, int alpha, int beta, int depth) {
    char move[3];
    moveToString(*m, move);
    play(&Game, move);
    if(m->x == -1 || depth > MAX_DEPTH) return evaluate(&Game);

    int value = 100;
    struct Move actions[32];
    getAvailableMoves(&Game, actions);
    for (int i = 0; actions[i].x != -1; i++) {
        int max = maxValue(Game, &actions[i], alpha, beta, depth+1);
        char tmp[3];
        moveToString(actions[i],tmp);
        printdepth(depth);
        printf("    %s: %i\n", tmp, max);
        value = (value < max) ? value : max;
        if(value <= alpha) return value;
        beta = (beta < value) ? beta : value;

    }
    return value;
}
