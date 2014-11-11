package com.alexlopashev.tuples.templates.procedure;

public interface TripleProcedure<L, M, R> {

    boolean execute(L left, M middle, R right);

}
