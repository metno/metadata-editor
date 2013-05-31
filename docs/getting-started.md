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

  * datastore.type: This is the type of data store. Set it to WebDAVDataStore
  * datastore.host: The WebDAV host URL without the protocol prefix like http://
  * datastore.protocol: The type of protocol that is used. Either http or https
  * datastore.defaultUser: The username for the default user. The default user is used for reading data from the repository.
  * datastore.defaultPassword: The password used for logging in as the default user.


## Structure of a metadata repository

All metadata repositories need to follow a specific structure for the metadata editor to read them. The folder structure is a follows
  
  * config: contains most of the configuration of the editor
    * setup.xml Contains configuration of supported XML formats
    * <FORMAT>Editor.xml: Contains the configuration of the editor for the format named <FORMAT>
    * <FORMAT>Template.xml: Contains the XML template for the format named <FORMAT>. The template is used both when reading the metadata files and when writing the metadata files.
  * XML: Contains all the metadata XML files that the editor can read and write.
    * metadatafile1.xml
    * metadatafile2.xml
    * ...
  * checkAccessOk.txt: Empty text file that is used to see if a user has write access to the repository.


## Example of a setup.xml file

The following is an example of a setup.xml file. 

    <?xml version="1.0" encoding="UTF-8"?>
    <editorSetup>
        <supportedMetadataFormats>
            <format tag="MMD">
                <detector type="rootNode">
                    <arg name="rootNode" value="mmd" />
                    <arg name="namespace" value="http://www.met.no/schema/mmd" />
                </detector>
            </format>
        </supportedMetadataFormats>
    </editorSetup>
    
In this file we have configured the metadata repository to support a single format we call MMD (the format used internally by The Norwegian Meteorological Institute). When the editor reads an metadata xml file it will then use a "detector" to determine the format of the file. In the case of MMD we detect the format by looking at the tag name and namespace of the root node in the XML file.

A single repository can support multiple formats if you want to. You just need to configure it in the setup.xml and the editor will automatically determine the correct template and editor configurations to use (more on these files in the next sections).

## An example <FORMAT>Template.xml file

