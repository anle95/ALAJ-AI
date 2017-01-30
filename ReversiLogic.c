#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef enum {
    None,
    Black,
    White
} Player;

void resetBoard(Player board[8][8]);
int onBoard(int a);
int play(Player board[8][8], char *move, Player p);
int viableMove(Player board[8][8], Player p, Player o, int a, int b);
void update(Player board[8][8], Player p, Player o, int a, int b);
int checkLine(Player board[8][8], Player p, Player o, int a, int b, int dirX, int dirY);
void turn(Player board[8][8], Player p, int a, int b, int dirX, int dirY);

void resetBoard(Player board[8][8]) {
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            board[i][j] = None;
        }
    }
    board[3][3] = board[4][4] = White;
    board[4][3] = board[3][4] = Black;
}

void printBoard(Player board[8][8], Player p, int disks[2]) {
    for (int i = 0; i < 8; i++) {
        printf("%c", '\n');
        for (int j = 0; j < 8; j++) {
            switch (board[i][j]) {
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
    char* pString = p == Black ? "\nBlack" : "\nWhite";
    printf("%s\n", pString);
    printf("%i", disks[0]);
    printf("%s", "-");
    printf("%i\n", disks[1]);
}

int onBoard(int a) {return (a >= 0 && a < 8);}

int play(Player board[8][8], char *move, Player p) {
    int a = move[0] - '1';
    Player o = (p == White) ? Black : White; //opponent
    if (!onBoard(a)) {
        printf("Invalid move\n");
        return 0;
    }
    int b = move[1] - 'a';
    if (!onBoard(b)) {
        printf("Invalid move\n");
        return 0;
    }
    if(viableMove(board, p, o, a, b)) { //Troligen ineffektivt, jag gör nästan samma sak igen i update
        update(board, p, o, a, b);
        return 1;
    } else {
        printf("Invalid move\n");
        return 0;
    }
}

void compMove(Player board[8][8], Player p, char *outputMove) {
    char move[3];
    Player o = (p == Black) ? White : Black;
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (viableMove(board, p, o, i, j)) {
                update(board, p, o, i, j);
                move[0] = i+'1';
                move[1] = j+'a';
                move[2] = '\0';
                strcpy(outputMove, move);
                return;
            }
        }
    }
}

int viableMove(Player board[8][8], Player p, Player o, int a, int b) {
    if (board[a][b] != None) return 0;
    for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            if (!(i == 0 && j == 0) && checkLine(board, p, o, a, b, i, j)) return 1;
        }
    }
    return 0;
}

void update(Player board[8][8], Player p, Player o, int a, int b) {
    board[a][b] = p;
    for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            if(checkLine(board, p, o, a, b, i, j)) {
                turn(board, p, a, b, i, j);
            }
        }
    }
}

int checkLine(Player board[8][8], Player p, Player o, int a, int b, int dirX, int dirY) {
    int x = a + dirX;
    int y = b + dirY;
    if(!(onBoard(x) && onBoard(y))) return 0;
    Player curr = board[x][y];
    if (curr == p) return 0;
    while (curr == o) {
        x += dirX;
        y += dirY;
        if(!(onBoard(x) && onBoard(y))) return 0;
        curr = board[x][y];
    }
    if (curr == p) return 1;
    else return 0;
}

void turn(Player board[8][8], Player p, int a, int b, int dirX, int dirY) {
    int x = a + dirX;
    int y = b + dirY;
    while (board[x][y] != p) {
        board[x][y] = p;
        x += dirX;
        y += dirY;
    }
}

int anyViableMove(Player board[8][8], Player p, Player o) {
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (viableMove(board, p, o, i, j)) return 1;
        }
    }
    return 0;
}

int* countDisks(Player board[8][8]) {
    int black = 0, white = 0;
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (board[i][j] == Black) {
                black++;
            } else if (board[i][j] == White) {
                white++;
            }
        }
    }
    int count[2] = {black, white};
    return count;
}

int main(void) {
    Player board[8][8] = {None};
    board[3][3] = board[4][4] = White;
    board[4][3] = board[3][4] = Black;
    Player currentP = Black;
    Player currentO = White;
    int disks[2] = {2, 2};
    printBoard(board, Black, disks);
    int gameOver = 0;

    char input[2];
   printf("Enter your color (Black or White): \n");
   char pChar[6];
   if (scanf("%s", pChar) != 1) {
        //exit program?
   }
   Player p = (strcmp(pChar, "Black") == 0) ? Black : White;
   char move[2];
   while (1) {
        if (!anyViableMove(board, currentP, currentO)) {
            currentO = currentP;
            currentP = (currentP == Black) ? White : Black;
            gameOver = 1;
            continue;
        }
        if (currentP == p) {
            if (scanf("%s", input) != 1)
            break;
            if(play(board, input, currentP)) {
                currentO = currentP;
                currentP = (currentP == Black) ? White : Black;
                gameOver = 0;
            }
        } else {
            compMove(board, currentP, move);
            printf("Computer plays: %s\n", move);
            currentO = currentP;
            currentP = (currentP == Black) ? White : Black;
            gameOver = 0;
        }
        *disks = countDisks(board);
        printBoard(board, currentP, *disks);
   }

    return 0;
}
