package com.chess.engine.board;

import com.chess.engine.piece.Piece;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    public static final Move NULL_MOVE = new NullMove(null, null, -1);
    public Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getCurrentCoordinate() {
        return this.getMovPiece()Piece.getPiecePosition();
    }
    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }
    // lấy dữ liệu 
    public Piece getMovPiece(){
        return this.movedPiece;
    }
    public abstract Board execute();

    public static final class NormalMove extends Move {
        public NormalMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        //nhiệm vụ tạo ra một bàn cờ mới sau khi một quân cờ di chuyển
        @Override
        public Board execute() {

            @SuppressWarnings("unused")
            //Tạo builder để dựng bàn cờ mới
            final Board.Builder builder = new Board.Builder();
            // giữ lại quân của current player (trừ quân di chuyển)
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            //duyệt quân đối thủ
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movPiece(this));
            // đổi lượt chơi
            builder.setMoveMaker(this.board.currentPlayer().getAlliance());

            return builder.build();
        }
    }

    public static final class AttackMove extends Move {
        final Piece attackedPiece;
        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }
    }

    
    public static final class PawnMove extends Move {
        public PawnMove(final Board board, 
                        final Piece movedPiece, 
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class PawnAttackMove extends AttackMove {
        public PawnAttackMove(final Board board, 
                              final Piece movedPiece, 
                              final int destinationCoordinate, 
                              final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }
    
    
    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(final Board board, 
                                      final Piece movedPiece, 
                                      final int destinationCoordinate, 
                                      final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnJump extends Move {
        public PawnJump(final Board board, 
                        final Piece movedPiece, 
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    static abstract class CastleMove extends Move {
        public CastleMove(final Board board, 
                          final Piece movedPiece, 
                          final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board, 
                                  final Piece movedPiece, 
                                  final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board, 
                                   final Piece movedPiece, 
                                   final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute null move");
        }

    }
    
    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Not instantiable!");
        }
        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {
            for(final Move move : board.getAllLegalMoves()) {
                if(move.getCurrentCoordinate() == currentCoordinate &&
                   move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return new NullMove(board, null, -1);
        }
    }
}
