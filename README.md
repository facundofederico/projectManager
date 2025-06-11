# Project Manager
Small project for practicing Java and Neo4j.

The main idea is to provide a solution that allows the user to create 'projects' 
and a directional graph of tasks within it. Later this graph can be used to calculate 
the project total duration using a critical path calculation, essentially creating
a Grantt chart. (Note: I could actually rename the project to something like that)

The project is divided in modules in such a way that allows the future development of
alternative storage providers (for now I wanted to take advantage of a graph database engine
such as Neo4j, but it could be implemented in a simple JSON file) and controllers 
(such as a CLI, a REST API or a visual UI).

For now the implementation only covers a tree structure, but with a few tweaks I should
be able to provide a full directional graph support. Measures will be taken to avoid
the creation of loops within the graph.