package com.base.ddd.shared.application;

/**
 * Base interface for Use Cases (Application Services)
 * Use Case đại diện cho một business operation/workflow
 *
 * @param <I> Input (Request/Command/Query)
 * @param <O> Output (Response/Result)
 */
public interface UseCase<I, O> {

    /**
     * Execute the use case with given input
     *
     * @param input The input data/command
     * @return The result of the operation
     * @throws Exception if execution fails
     */
    O execute(I input) throws Exception;
}