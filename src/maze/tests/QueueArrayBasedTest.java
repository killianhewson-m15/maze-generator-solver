package maze.tests;

import maze.structures.QueueArrayBased;
import maze.structures.QueueException;

// Checks queue capacity, FIFO order, wraparound and error handling.
public class QueueArrayBasedTest
{
    // Tests custom capacities and checks the original default capacity is unchanged.
    public static void main(String[] args)
    {
        for (int capacity : new int[] {1, 3, 50, 900})
        {
            checkCapacity(capacity);
            System.out.println("PASS: capacity " + capacity);
        }

        QueueArrayBased defaultQueue = new QueueArrayBased();

        for (int i = 0; i < 50; i++)
        {
            defaultQueue.enqueue(i);
        }

        check(defaultQueue.isFull(),
                "Default capacity should still be 50.");

        System.out.println("PASS: default capacity");

        for (int capacity : new int[] {0, -1})
        {
            try
            {
                new QueueArrayBased(capacity);
                throw new IllegalStateException(
                        "Invalid capacity was accepted.");
            }
            catch (IllegalArgumentException expected)
            {
                // Correct: invalid capacities must be rejected.
            }
        }

        System.out.println("PASS: invalid capacities rejected");
        System.out.println("All queue checks passed.");
    }

    // Checks normal queue operations, overflow, underflow and reuse after clearing.
    private static void checkCapacity(int capacity)
    {
        QueueArrayBased queue = new QueueArrayBased(capacity);
        check(queue.isEmpty(), "New queue should be empty.");

        for (int i = 0; i < capacity; i++)
        {
            queue.enqueue(i);
        }

        check(queue.isFull(), "Queue should be full.");

        try
        {
            queue.enqueue("extra");
            throw new IllegalStateException(
                    "Full queue accepted another item.");
        }
        catch (QueueException expected)
        {
            // Correct: enqueue must reject an item when full.
        }

        check(queue.dequeue().equals(0), "First item should be 0.");

        // Reuse the space at the beginning of the circular array.
        queue.enqueue(capacity);

        for (int i = 1; i <= capacity; i++)
        {
            check(queue.peek().equals(i),
                    "Incorrect front item after wraparound.");

            check(queue.dequeue().equals(i),
                    "Incorrect FIFO order after wraparound.");
        }

        check(queue.isEmpty(), "Queue should be empty after removal.");

        try
        {
            queue.dequeue();
            throw new IllegalStateException(
                    "Empty queue allowed a removal.");
        }
        catch (QueueException expected)
        {
            // Correct: dequeue must reject removal when empty.
        }

        // Clearing must empty the queue without changing its capacity.
        queue.enqueue("reset check");
        queue.dequeueAll();

        check(queue.isEmpty(), "Reset should empty the queue.");

        for (int i = 0; i < capacity; i++)
        {
            queue.enqueue(i);
        }

        check(queue.isFull(),
                "Reset should preserve the chosen capacity.");
    }

    // Stops the test immediately if a required condition fails.
    private static void check(boolean condition, String message)
    {
        if (!condition)
        {
            throw new IllegalStateException(message);
        }
    }
}