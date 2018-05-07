// Clear database
match (x) detach delete x;

// Configuration
create (config:configuration{
  simulationDurationTime:60,   // in seconds
  simulationTimeStep:500       // in milliseconds
});

// Create route with lanterns
create
  (lantern1:lantern{nodeId:"1", x:0, y:0})
  -[:CONNECTED_TO]->(lantern2:lantern{nodeId:"2", x:10, y:0})
  -[:CONNECTED_TO]->(lantern3:lantern{nodeId:"4", x:0, y:10});
create (lantern4:lantern{nodeId:"3", x:10, y:10});
match (a:lantern{nodeId:"4"}), (b:lantern{nodeId:"1"}) create (a)-[:CONNECTED_TO]->(b);
match (a:lantern{nodeId:"2"}), (b:lantern{nodeId:"3"}) create (a)-[:CONNECTED_TO]->(b);
match (a:lantern{nodeId:"3"}), (b:lantern{nodeId:"4"}) create (a)-[:CONNECTED_TO]->(b);

// Create tracking devices
create (camera1:camera{nodeId:"1", radius:4.0, angle:15});
create (vds1:velocity_and_direction_sensor{nodeId:"1", radius:6.0, angle:15});
create (ms1:movement_sensor{nodeId:"1", radius:5.0});

// Attach all tracking devices to each lantern
match (n:lantern), (c:camera) create (n)-[:HAS]->(c);
match (n:lantern), (ms:movement_sensor) create (n)-[:HAS]->(ms);
match (n:lantern), (vds:velocity_and_direction_sensor) create (n)-[:HAS]->(vds);

// Actors
// startNodeId: id of the lantern where actor starts
// targetNodeId: id of the lantern toward which the actor starts his movement (must be connected by road with lantern with startNodeId)
// velocity: velocity in meters per second
create (actor1:vehicle{id:"KR01234", startNodeId:"1", targetNodeId:"2", velocity:3.0});
create (actor2:pedestrian{startNodeId:"1", targetNodeId:"2", velocity:1.0});