package game;
import java.util.*;
public class games
{
    static Scanner sc = new Scanner(System.in);
    static String currentRoom = "Entrance";
    static String previousRoom = null;
    static Map<String, Map<String, String>> region = new HashMap<>();
    static List<String> inventory = new ArrayList<>();
    static int Health = 100;
    static boolean Won = false;
    static int goldCoins = 0;

    public static void main(String[] args)
    {
        initiate();
        System.out.println("Welcome to the Text-Adventure Game!");
        System.out.println("Type 'help' for a list of commands.\n");

        while (Health > 0 && !Won)
        {
            displayInfo();
            System.out.print("> ");
            String command = sc.nextLine().toLowerCase();
            commands(command);
        }
        if (Health <= 0)
        {
            System.out.println("Game Over! You have been defeated.");
        }
    }

    static void initiate()
    {
        region.put("Entrance", Map.of("north", "Forest", "east", "Dungeon"));
        region.put("Forest", Map.of("south", "Entrance", "east", "Treasure Room"));
        region.put("Dungeon", Map.of("west", "Entrance", "east", "Cavern"));
        region.put("Cavern", Map.of("west", "Dungeon"));
        region.put("Treasure Room", Map.of("west", "Forest"));
    }

    static void displayInfo()
    {
        System.out.println("\nYou are in the " + currentRoom + ".");
        switch (currentRoom)
        {
            case "Entrance":
                System.out.println("A grand entrance with paths leading north to the Forest and east to the Dungeon.");
                break;
            case "Forest":
                System.out.println("A mystical forest with chirping birds and a hint of danger.");
                break;
            case "Dungeon":
                System.out.println("A dark, damp dungeon. Beware of traps and enemies.");
                break;
            case "Cavern":
                System.out.println("A spooky cavern with echoes of unknown creatures.");
                break;
            case "Treasure Room":
                System.out.println("The legendary Treasure Room! Find the treasure to win.");
                break;
            default:
                System.out.println("A mysterious room filled with wonder.");
        }
        System.out.println("Available directions to move: " + region.getOrDefault(currentRoom, Map.of()).keySet());
    }

    static void commands(String command)
    {
        if (command.startsWith("go "))
        {
            move(command.substring(3));
        }
        else if (command.equals("check inventory"))
        {
            checkInventory();
        }
        else if (command.equals("use potion"))
        {
            usePotion();
        }
        else if (command.equals("talk"))
        {
            talkToNPC();
        }
        else if (command.equals("attack"))
        {
            combat();
        }
        else if (command.equals("run"))
        {
            runAway();
        }
        else if (command.equals("rest"))
        {
            rest();
        }
        else if (command.equals("search"))
        {
            searchRoom();
        }
        else if (command.equals("help"))
        {
            displayHelp();
        }
        else
        {
            System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }

    static void move(String direction)
    {
        Map<String, String> exits = region.get(currentRoom);
        if (exits != null && exits.containsKey(direction))
        {
            previousRoom = currentRoom;
            currentRoom = exits.get(direction);

            if (currentRoom.equals("Treasure Room") && inventory.contains("Key"))
            {
                System.out.println("You unlock the Treasure Room and claim the treasure!");
                System.out.println("congratulations!!! You Won!!");
                Won = true;
            }
            else
            {
                Events();
            }
        }
        else
        {
            System.out.println("You can't go that way.");
        }
    }

    static void checkInventory()
    {
        System.out.println("Inventory: " + (inventory.isEmpty() ? "Empty" : inventory));
        System.out.println("Gold Coins: " + goldCoins);
        System.out.println("Health: " + Health);
    }

    static void usePotion()
    {
        if (inventory.contains("Potion"))
        {
            System.out.println("You drink a Potion and restore health by 50!");
            Health = Math.min(Health + 50, 100);
            inventory.remove("Potion");
        }
        else
        {
            System.out.println("You don't have any Potions.");
        }
    }

    static void talkToNPC()
    {
        if (currentRoom.equals("Forest"))
        {
            System.out.println("A wizard gives you a hint: 'The treasure lies to the east! Take this Key to unlock its secrets.'");
            if (!inventory.contains("Key"))
            {
                inventory.add("Key");
            }
        }
        else if (currentRoom.equals("Entrance"))
        {
            System.out.println("An old man offers you a Potion!");
            if (!inventory.contains("Potion"))
            {
                inventory.add("Potion");
            }
        }
        else
        {
            System.out.println("There is no one here to talk to.");
        }
    }

    static void combat()
    {
        if (!currentRoom.equals("Dungeon") && !currentRoom.equals("Cavern"))
        {
            System.out.println("There's nothing to fight here.");
            return;
        }

        Random random = new Random();
        System.out.println("An enemy attacks you!");
        int enemyHealth = 50;

        while (enemyHealth > 0 && Health > 0)
        {
            System.out.print("Do you want to 'attack' or 'run'? ");
            String action = sc.nextLine().toLowerCase();

            if (action.equals("attack"))
            {
                int playerDamage = random.nextInt(20) + 5;
                int enemyDamage = random.nextInt(15) + 5;

                System.out.println("You deal " + playerDamage + " damage!");
                enemyHealth -= playerDamage;

                if (enemyHealth <= 0)
                {
                    System.out.println("You defeated the enemy!");
                    if (random.nextBoolean())
                    {
                        inventory.add("Potion");
                        System.out.println("The enemy dropped a Potion!");
                    }
                    else
                    {
                        goldCoins += 20;
                        System.out.println("The enemy dropped 20 gold coins!");
                    }
                    return;
                }

                System.out.println("The enemy deals " + enemyDamage + " damage!");
                Health -= enemyDamage;
                if (Health <= 0)
                {
                    System.out.println("You have been defeated!");
                    return;
                }
            }
            else if (action.equals("run"))
            {
                runAway();
                return;
            }
            else
            {
                System.out.println("Invalid action. Type 'attack' or 'run'.");
            }
        }
    }

    static void runAway()
    {
        if (previousRoom != null)
        {
            System.out.println("You flee from the fight and return to the " + previousRoom + "!");
            currentRoom = previousRoom;
        }
        else
        {
            System.out.println("There's nowhere to run!");
        }
    }

    static void rest()
    {
        System.out.println("You take a rest to recover your health...");
        Random rand = new Random();
        if (rand.nextBoolean())
        {
            System.out.println("You restored 20 health.");
            Health = Math.min(Health + 20, 100);
        }
        else
        {
            System.out.println("You are sufficient with resting!");
            combat();
        }
    }

    static void searchRoom()
    {
        if (currentRoom.equals("Dungeon"))
        {
            System.out.println("You found a secret passage leading to a hidden room!");
            region.put("Dungeon", Map.of("west", "Entrance", "east", "Cavern", "north", "Secret Room"));
            region.put("Secret Room", Map.of("south", "Dungeon"));
        }
        else
        {
            System.out.println("You found nothing special.");
        }
    }

    static void Events()
    {
        Random rand = new Random();
        if (rand.nextBoolean())
        {
            System.out.println("You stepped on a trap and lost 10 health!");
            Health -= 10;
            if (Health <= 0)
            {
                System.out.println("The trap was terminated!");
            }
        }
        else
        {
            System.out.println("You find 10 gold coins!");
            goldCoins += 10;
        }
    }

    static void displayHelp()
    {
        System.out.println("Available commands:");
        System.out.println("- go [north/south/east/west]: Move to a specified direction.");
        System.out.println("- check inventory: View your collected items and stats.");
        System.out.println("- use potion: Restore 50 health (if you have a Potion).");
        System.out.println("- talk: Interact with an NPC.");
        System.out.println("- attack: Fight an enemy.");
        System.out.println("- run: Flee to the previous room during combat.");
        System.out.println("- rest: Regain health with a chance of ambush.");
        System.out.println("- search: Look for hidden secrets in the room.");
    }
}