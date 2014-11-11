package com.alexlopashev.tuples.templates.procedure;

public interface DoubleProcedure<L, R> {

    boolean execute(L left, R right);

}
