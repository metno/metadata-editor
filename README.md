# metadata-editor

metadata-editor is a generic web-based metadata editor that can be configured
for editing of most XML based metadata format (e.q. ISO-19139, DIf).

## Requirements

  * Tomcat 7 (or another servlet container, but only Tomcat 7 has been tested)
  * Java 7
  * WebDAV server (optional)
  * Maven 2 (only for building the project)

## Building and installation

To build the project first checkout it out from the code repository. Then on the
commandline "cd" to the directory where you checked out the source code. Then type

    mvn package

All dependencies should be downloaded, the project built and then packaged. The packaging 
will produce a metadata-editor.war file in the target/ directory. It will also create a 
Debian package that can be used for installation on Debian based systems.

To install the metadata-editor, deploy the metadata-editor.war file to you servlet
container.

## How to configure

See docs/getting-started.md in the source code.
