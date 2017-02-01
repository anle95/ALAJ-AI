#ifndef REVERSI_H
#define REVERSI_H

typedef enum {
    None,
    Black,
    White
} Player;

struct GameState {
    Player board[8][8];
    Player currentP, currentO;
    int black;
    int white;
};

void resetGame(struct GameState *Game);
void printBoard(struct GameState *Game);
int play(struct GameState *Game, char *move);
void compMove(struct GameState *Game, char *outputMove);
int viableMove(struct GameState *Game, int a, int b);
int anyViableMove(struct GameState *Game);
void findCompMove (struct GameState Game, char *outputMove);


#endif
