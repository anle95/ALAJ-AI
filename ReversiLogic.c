#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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

// struct GameNode {
//     GameNode parent, *children;
//     Player board[8][8];
//     Player p, o;
// };

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

void resetGame(struct GameState *Game) {
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            Game->board[i][j] = None;
        }
    }
    Game->board[3][3] = Game->board[4][4] = White;
    Game->board[4][3] = Game->board[3][4] = Black;
    Game->currentP = Black;
    Game->currentO = White;
    Game->black = 2;
    Game->white = 2;
}

void printBoard(struct GameState *Game) {
    for (int i = 0; i < 8; i++) {
        printf("\n");
        for (int j = 0; j < 8; j++) {
            switch (Game->board[i][j]) {
                case White:
                    printf("%c", 'W');
                    break;
                case Black:
                    printf("%c", 'B');
                    break;
                default:
                    printf("%c", '_');
                    break;
            }
        }
    }
    char* pString = Game->currentP == Black ? "Black" : "White";
    printf("\n%i - %i\n%s to move\n", Game->black, Game->white, pString);
}

int onBoard(int a) {
    return (a >= 0 && a < 8);
}

int play(struct GameState *Game, char *move) {
    int a = move[0] - '1';
    if (!onBoard(a)) {
        printf("Invalid move\n");
        return 0;
    }
    int b = move[1] - 'a';
    if (!onBoard(b)) {
        printf("Invalid move\n");
        return 0;
    }
    if(viableMove(Game, a, b)) { //Troligen ineffektivt, jag gör nästan samma sak igen i update
        update(Game, a, b);
        return 1;
    } else {
        printf("Invalid move\n");
        return 0;
    }
}

void compMove(struct GameState *Game, char *outputMove) {
    char move[3];
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (viableMove(Game, i, j)) {
                update(Game, i, j);
                move[0] = i+'1';
                move[1] = j+'a';
                move[2] = '\0';
                strcpy(outputMove, move);
                return;
            }
        }
    }
}

int viableMove(struct GameState *Game, int a, int b) {
    if (Game->board[a][b] != None) return 0;
    for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            if (!(i == 0 && j == 0) && checkLine(Game, a, b, i, j)) return 1;
        }
    }
    return 0;
}

void update(struct GameState *Game, int a, int b) {
    Game->board[a][b] = Game->currentP;
    if (Game->currentP == White) Game->white++;
    else                         Game->black++;

    for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            if(checkLine(Game, a, b, i, j)) {
                turn(Game, a, b, i, j);
            }
        }
    }
}

int checkLine(struct GameState *Game, int a, int b, int dirX, int dirY) {
    int x = a + dirX;
    int y = b + dirY;
    if(!(onBoard(x) && onBoard(y))) return 0;
    Player curr = Game->board[x][y];
    if (curr == Game->currentP) return 0;
    while (curr == Game->currentO) {
        x += dirX;
        y += dirY;
        if(!(onBoard(x) && onBoard(y))) return 0;
        curr = Game->board[x][y];
    }
    if (curr == Game->currentP) return 1;
    else return 0;
}

//returnerar antalet vända disks
void turn(struct GameState *Game, int a, int b, int dirX, int dirY) {
    int x = a + dirX;
    int y = b + dirY;
    int turned = 0;
    while (Game->board[x][y] != Game->currentP) {
        Game->board[x][y] = Game->currentP;
        turned++;
        x += dirX;
        y += dirY;
    }

    if (Game->currentP == White) {
        Game->white += turned;
        Game->black -= turned;
    } else {
        Game->black += turned;
        Game->white -= turned;
    }
}

int anyViableMove(struct GameState *Game) {
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (viableMove(Game, i, j)) return 1;
        }
    }
    return 0;
}

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
            compMove(&Game, computerMove);
            printf("Computer plays: %s\n", computerMove);
            Game.currentO = Game.currentP;
            Game.currentP = (Game.currentP == Black) ? White : Black;
            gameOver = 0;
        }
        printBoard(&Game);
    }

    return 0;
}
