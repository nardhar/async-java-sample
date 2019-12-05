import java.util.concurrent.CompletableFuture;

public class AsyncApp {

    public CompletableFuture<Integer> divide() {
        return CompletableFuture.completedFuture(1)
        .thenApply(value -> {
            // not controlled exception
            return value / 0;
        })
        .exceptionally(ex -> {
            System.out.println("First error: " + ex.getClass());
            // .exceptionally method could return an int
            // or throw a new Exception (It could be a custom one)
            throw new RuntimeException("Custom exception");
        });
    }

    public static void main(String[] args) {
        try {
            new AsyncApp().divide()
            .thenApply(result -> {
                System.out.println(result);
                return "division completed";
            })
            .exceptionally(ex -> {
                System.out.println("Second error: " + ex.getClass());
                // it throws a new Exception by getting the cause
                // of the catched CompletionException
                throw new RuntimeException(ex.getCause().getMessage());
            })
            .join();
        } catch(Exception ex) {
            // CompletableFuture always throws a CompletionException
            System.out.println("Catched error: " + ex.getClass());
            // its cause is the exception thrown in the execution chain
            Throwable cause = ex.getCause();
            System.out.println("Catched error cause: " + cause);
            System.out.println("Cause message: " + cause.getMessage());
        }
    }

}
