package org.lambda.exceptions;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExceptionsInLambdaTests {

    private List<String> integers = Arrays.asList("1", "2", "3s", "4", "5");

    @Test
    public void givenLambdaExceptionThenUseClunkyCatchBlock() throws Exception {
        List<Integer> collected = integers.stream().map(item -> {
                    try {
                        return Integer.parseInt(item);
                    } catch (NumberFormatException nfe) {
                        System.err.printf("Item %s not a number. Used 1 instead.", item);
                        return 1;
                    }
                }
        ).collect(Collectors.toList());
        System.out.println(collected);
        Assert.assertFalse(collected.contains(3));
    }

    @Test(expected = NumberFormatException.class)
    public void givenLambdaExceptionThenStreamTerminated() throws Exception {
        List<Integer> colleced = integers.stream().map(item -> Integer.parseInt(item)).collect(Collectors.toList());
        Assert.assertNull(colleced);
    }

    @Test
    public void givenLambdaExceptionThenUseEitherUtilityClass() throws Exception {
        List<Optional> optionals = integers.stream()
                .map(Either.applyRight(Integer::parseInt))
                .filter(Either::isRight)
                .map(Either::getRight)
                .collect(Collectors.toList());
        System.out.println(optionals);
        Assert.assertEquals(optionals.size(), 4);
    }
}

/**
 * Represents a function which accepts one argument and produce result and can throw {@link Exception} Exception.
 *
 * @param <T> type of input to the function
 * @param <R> type of result
 * @author rgavrysh
 */
@FunctionalInterface
interface CheckedFunction<T,R>{

    /**
     * Applies this function to the argument
     *
     * @param t function argument
     * @return function result
     * @throws Exception can be thrown while performing operation on argument
     */
    R apply(T t) throws Exception;
}

/**
 * A container object which has two sides with values either can be null but not at the same time.
 * Can be considered as an extension to {@link Optional} Optional.
 *
 * @param <L> type of left values; implies to negative or not acceptable values like Exceptions, nulls etc.
 * @param <R> type of right values; implies to acceptable and valuable to your needs values
 */
class Either<L, R> {
    private final L left;
    private final R right;

    private Either(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Either<L, R> Left(L value) {
        return new Either<>(value, null);
    }

    public static <L, R> Either<L, R> Right(R value) {
        return new Either<>(null, value);
    }

    public Optional<L> getLeft() {
        return Optional.ofNullable(left);
    }

    public Optional<R> getRight() {
        return Optional.ofNullable(right);
    }

    public boolean isLeft() {
        return left != null;
    }

    public boolean isRight() {
        return right != null;
    }

    public <T> Optional<T> mapLeft(Function<? super L, T> mapper) {
        if (isLeft()) {
            return Optional.of(mapper.apply(left));
        }
        return Optional.empty();
    }

    public <T> Optional<T> mapRight(Function<? super R, T> mapper) {
        if (isRight()) {
            return Optional.of(mapper.apply(right));
        }
        return Optional.empty();
    }

    /**
     * Transform function which throws exception into function with {@link Either} Either result type
     *
     * @param function function which can throw Exception
     * @param <T> type of input to the function
     * @param <R> type of result
     * @return generic {@link Function} Function{@literal <T, Either>} with T as an input and Either as a result
     */
    public static <T, R> Function<T, Either> applyRight(CheckedFunction<T, R> function) {
        return t -> {
            try {
                return Either.Right(function.apply(t));
            } catch (Exception e) {
                return Either.Left(e);
            }
        };
    }

    @Override
    public String toString() {
        if (isLeft()) {
            return "{left:" + left + "}";
        }
        return "{right:" + right + "}";
    }
}