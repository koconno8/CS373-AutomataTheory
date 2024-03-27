import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

class State 
{
    boolean isStart;
    boolean isAccept;

    State() 
    {
        isStart = false;
        isAccept = false;
    }
}

class Transition
{
    int fromState;
    char symbol;
    int toState;

    Transition(int fromState, char symbol, int toState)
    {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }
}

public class oconnor_p1
{
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("Need 2 Arguments!!");
            System.exit(0);
        }

        String machineDef = args[0];
        String simulateString = args[1];

        // Initialize structures to store states and transitions
        Map<Integer, State> states = new HashMap<>(); // hashmap because it makes it easy to check and call states
        List<Transition> transitions = new ArrayList<>();// arraylist because it dynamically resizes
        
        int maxStateNumber = -1; // Initialize to a negative so it will fail if there are no states

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(machineDef));
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] machine = line.split("\\s+");
                if (machine[0].equals("state"))
                {
                    int stateNum = Integer.parseInt(machine[1]);
                    if (stateNum > maxStateNumber)
                    {
                        maxStateNumber = stateNum; 
                    }
                    State state = new State();
                    for (int i = 2; i < machine.length; i++)
                    {
                        if (machine[i].equals("start"))
                        {
                            state.isStart = true;
                        }
                        else if (machine[i].equals("accept"))
                        {
                            state.isAccept = true;
                        }
                    }
                    states.put(stateNum, state);
                }
                else if (machine[0].equals("transition"))
                {
                    int fromState = Integer.parseInt(machine[1]);
                    char symbol = machine[2].charAt(0);
                    int toState = Integer.parseInt(machine[3]);
                    if (fromState > maxStateNumber)
                    {
                        maxStateNumber = fromState; 
                    }
                    if (toState > maxStateNumber)
                    {
                        maxStateNumber = toState; 
                    }
                    transitions.add(new Transition(fromState, symbol, toState));
                }
            }
            reader.close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            System.exit(0);
        }

        for (int i = 0; i <= maxStateNumber; i++)
        {
            states.putIfAbsent(i, new State());
        }

        // Initialize the current states with start states
        Set<Integer> currentStates = new HashSet<>();
        for (Map.Entry<Integer, State> entry : states.entrySet())
        {
            if (entry.getValue().isStart)
            {
                currentStates.add(entry.getKey());
            }
        }

        // Simulate the NFA
        for (int i = 0; i < simulateString.length(); i++)
        {
            char symbol = simulateString.charAt(i);
            Set<Integer> nextStates = new HashSet<>();
            for (int currentState : currentStates)
            {
                for (Transition transition : transitions)
                {
                    if (transition.fromState == currentState && transition.symbol == symbol)
                    {
                        nextStates.add(transition.toState);
                    }
                }
            }
            currentStates = nextStates;
        }

        // Divide the accept states from the final current states
        Set<Integer> acceptStates = new HashSet<>();
        for (int state : currentStates)
        {
            if (states.get(state).isAccept)
            {
                acceptStates.add(state);
            }
        }

        // Print the results
        if (!acceptStates.isEmpty())
        {
            System.out.print("accept ");
            for (int state : acceptStates)
            {
                System.out.print(state + " ");
            }
        } 
        else
        {
            System.out.print("reject ");
            for (int state : currentStates)
            {
                System.out.print(state + " ");
            }
        }
        System.out.println();
    }
}