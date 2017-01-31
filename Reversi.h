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
int onBoard(int a);
int play(struct GameState *Game, char *move);
void compMove(struct GameState *Game, char *outputMove);
int viableMove(struct GameState *Game, int a, int b);
void update(struct GameState *Game, int a, int b);
int checkLine(struct GameState *Game, int a, int b, int dirX, int dirY);
void turn(struct GameState *Game, int a, int b, int dirX, int dirY);
int anyViableMove(struct GameState *Game);
void findCompMove (struct GameState *Game, int depth, char *outputMove);
int evalFunc(struct GameState *Game);


#endif
