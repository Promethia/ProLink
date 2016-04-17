## The Promethia Link
MC 1.9.2 server plugin for adding trains and control signs for creating and managing subways, railsystems, etc.

### Features 

#### Trains
The trains this plugin add to the game are quite similiar to those of the the [TrainCart](https://www.spigotmc.org/resources/traincarts.8557/) plugin.
 - Trains are being pulled by a locomotive (furnace minecart)
 - Up to 11 connected carts
 - Launch directions

#### Control Signs
The control signs will also work quite similiar to the TrainCart plugin. To create a control sign for ProLink, use '[prolink]' for
the first line of the sign. When a sign is triggered by a redstone signal, it'll find the closest rail block and use that location
for what it's supposed to do. So when spawning a train, it uses this location to spawn the locomotive.

For the second line use any of the commands listed below. The third and fourth lines have no meaning to the plugin as of yet.

#### Commands
The ProLink command syntax is as follows: **/plink [action] { [action_params] }**.

The following actions are currently available:
 - **spawn [direction] [carts] { [x] [y] [z] }**
 
   Uses location of the entity issueing the command (player, sign, etc).
   
   *Direction* can be either one of n, e, s or w
   
   *Carts* is the amount of carts excluding the locomotive
   
   *x, y, z (optional)* world coordinates as whole numbers. Only required when command is issued by the server itself. When issued
   by a player or entity, this will have precedence over the entities location
 - **removeall**
 
   Removes all active trains. This also removes the minecarts from the world

### Builds

#### Latest build:

[0.1 DEV](http://link.linksoft.io/prolink-latest.jar) - initial build

#### Previous builds:

I've just started! Give me a break...
