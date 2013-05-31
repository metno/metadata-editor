# Introduction

This documentation describes how you can get started configuring the editor. It assumes that you have already installed the application on a server.

## Setup the first metadata repository

The metadata editor does not store any information it self, but it is instead configured to read and write files to one or more metadata repositories.

Each metadata repository will contain the XML files that the editor reads and writes. 

The metadata editor can read and write files either using a common file protocol or over WebDAV. Our recommendation is to use Subversion as a metadata store since it supports WebDAV and can be setup to provide automatic versioning of you metadata files.

To add a new metadata repository to the editor you need to create a Java properties file and tell the metadata editor where to find the file. This is done by assigning the path to the environment variable METADATA_EDITOR_PROPERTIES. How to set the environment variable for your application server will depend on your server environment and your application server. For Tomcat on Ubuntu it is best to add the property to the /etc/defaults/tomcat7 file like this

    METADATA_EDITOR_PROPERTIES=/etc/tomcat7/metadataeditor.properties

The content of the metadataeditor.properties files should like this

    projects=repo_name
    repo_name.datastore.type=WebDAVDataStore
    repo_name.datastore.host=<path to Subversion or other type of WebDAV server>
    repo_name.datastore.protocol=https
    repo_name.datastore.defaultUser=<name of default user>
    repo_name.datastore.defaultPassword=<password of the default user>

The first line in the properties file i "projects". This is a comma separated list of project/repository names. The repository names are then re-used in the rest of the configuration.

For each repostory you need to specify the following

  * type
  * host
  * protocol
  * defaultUser
  * defaultPassword

