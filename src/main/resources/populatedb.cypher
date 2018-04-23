// Clear database
match (x) detach delete x;

// Create route with lanterns
create
  (lantern1:lantern{node_id:"1", x:0, y:0})
  -[:CONNECTED_TO]->(lantern2:lantern{node_id:"2", x:10, y:0})
  -[:CONNECTED_TO]->(lantern3:lantern{node_id:"3", x:0, y:10});
match (a:lantern{node_id:"3"}), (b:lantern{node_id:"1"}) create (a)-[:CONNECTED_TO]->(b);

// Create tracking devices
create (camera1:camera{node_id:"1", radius:4.0});
create (ms1:movement_sensor{node_id:"1", radius:5.0});
create (vds1:velocity_and_direction_sensor{node_id:"1", radius:6.0});

// Attach all tracking devices to each lantern
match (n:lantern), (c:camera) create (n)-[:HAS]->(c);
match (n:lantern), (ms:movement_sensor) create (n)-[:HAS]->(ms);
match (n:lantern), (vds:velocity_and_direction_sensor) create (n)-[:HAS]->(vds);