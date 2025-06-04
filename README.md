# To reviewers

## How to run

Just a plain Java app. Run it from the command line and you will get the usage instructions, 
that is a list of commands to interact with the Megaverse.

## Comments

This was quite a fun challenge. And I appreciate not having a specific time limit (I ended up using about 7 hours).

Below some unordered comments about architecture and other design choices.

Firstly, might be useful to start from "Main" class, since is the entry point of the application, you can easily
understand the flow from there.

When watching the package structure, you will realize is a simplified hexagonal architecture. 
To be honest I could have gone with three-layered, 
mvc or even a "figure out as you go" approach, considering was a small test. In any case
I did assume it was useful to have both "detachable" inputs and outputs (and also was more fun). 
The "repository" 
(your http API in this case) is the only driven port, the Main class is the only driver, via CLI. 
It should be easy to create an in-memory repository, to write to a kafka queue, or to expose 
endpoints that do the same as the CLI

Why is a simplified hexagonal architecture? Because you will not see any driver ports, Main class just
calls "HexagonApi". A spring controller or a kafka consumer could call that class as well.

You will realize the app relies heavily on the Megaverse class, the JSON received is converted to it, 
and any changes to the Megaverse are first done to a Megaverse object. Is inside the hexagon, everything should
be converted to it. Also, it is not anemic, it does have meaningful logic.

Depending on your experience, you might like or dislike that there are no dedicated mappers. It was just to 
speed up development; preferred to spend the time doing more meaningful things.

About the "CmMegaverseWriterRepository", I have an ugly Thread.sleep. I am aware of it, 
and will fix it in future commits (just as an excuse to practice concurrency). The sleep was the 
quickest solution to your API limits. More specialized solutions could make requests in batches, 
and only wait after getting the 429.

There is some error handling, very simple, mostly about catching exceptions and printing the messages. Validation
is also present, mostly in the form of "if" statements.

Finally, I ended up coupling a bit the CLI with the hexagon, by using "System.out.println" directly inside the
hexagon. Again, I am aware, I did not mind breaking the architecture a bit in order to give you something more
user friendly (the other option was to spend more time, of course)

