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

The templates files is the core of how the metadata editor reads and writes the metadata files. The template files are basically in the same format as the format as your metadata XML files, but mixed with template tags for the metadata editor. This is easiest explained by and example.


    <?xml version="1.0" encoding="UTF-8"?>
    <edt:editorDataTypes xmlns:edt="http://www.met.no/schema/metadataeditor/editorDataTypes" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.met.no/schema/metadataeditor/editorDataTypes dataTypes.xsd">

        <mmd:mmd xmlns:mmd="http://www.met.no/schema/mmd" xmlns:gml="http://www.opengis.net/gml">
            <mmd:metadata_version>1</mmd:metadata_version>        
            <mmd:dataset_language>en</mmd:dataset_language>

            <edt:string varName="metadata_identifier" minOccurs="1" maxOccurs="1">
                <mmd:metadata_identifier>$str</mmd:metadata_identifier>
            </edt:string>

            <edt:string varName="title_en" minOccurs="0" maxOccurs="1" xpath="/mmd:mmd/mmd:title[lang('en')]">
                <mmd:title xml:lang="en">$str</mmd:title>
            </edt:string>
            
            <edt:string varName="title_no" minOccurs="0" maxOccurs="1" xpath="/mmd:mmd/mmd:title[lang('no')]">
                <mmd:title xml:lang="no">$str</mmd:title>
            </edt:string>            
            
            <edt:list varName="iso_topic_category" minOccurs="0" maxOccurs="1" resource="isoTopicCategories.txt">
                <mmd:iso_topic_category>$listElement</mmd:iso_topic_category>
            </edt:list>   
            
            <mmd:geographic_extent>
                <edt:lonLatBoundingBox varName="geographic_extent_rectangle" minOccurs="0" maxOccurs="1">
                    <mmd:rectangle>
                        <mmd:north>$north</mmd:north>
                        <mmd:south>$south</mmd:south>
                        <mmd:east>$east</mmd:east>
                        <mmd:west>$west</mmd:west>
                    </mmd:rectangle>
                </edt:lonLatBoundingBox>
            </mmd:geographic_extent>
            
            <edt:container varName="data_access" minOccurs="0" maxOccurs="unbounded">
                <mmd:data_access>
                    <edt:string varName="data_access_name" minOccurs="1" maxOccurs="1">
                        <mmd:name>$str</mmd:name>
                    </edt:string>
                    <edt:uri varName="data_access_resource" minOccurs="1" maxOccurs="1">
                        <mmd:resource>$uri</mmd:resource>
                    </edt:uri>
                </mmd:data_access>
            </edt:container>  
        </mmd:mmd>
    </edt:editorDataTypes>            
    
The goal of this template is be able to edit XML files that look like this:

    <?xml version="1.0" encoding="UTF-8"?>
    <mmd:mmd xmlns:mmd="http://www.met.no/schema/mmd" xmlns:gml="http://www.opengis.net/gml">
        <mmd:metadata_version>1</mmd:metadata_version>        
        <mmd:dataset_language>en</mmd:dataset_language>

        <mmd:metadata_identifier>xyz-123</mmd:metadata_identifier>    
        <mmd:title xml:lang="en">Some dataset</mmd:title>  
        <mmd:title xml:lang="no">Et datasett</mmd:title>   
        <mmd:iso_topic_category>oceans</mmd:iso_topic_category>

        <mmd:geographic_extent>
            <mmd:rectangle>
                <mmd:north>90</mmd:north>
                <mmd:south>0</mmd:south>
                <mmd:east>43.2</mmd:east>
                <mmd:west>-20.1</mmd:west>
            </mmd:rectangle>
        </mmd:geographic_extent>
        
        <mmd:data_access>
            <mmd:name>WMS</mmd:name>
            <mmd:resource>http://example.org/wms</mmd:resource>
        </mmd:data_access>
        
        <mmd:data_access>
            <mmd:name>OPeNDAP</mmd:name>
            <mmd:resource>http://example.org/opendap</mmd:resource>
        </mmd:data_access>

    </mmd:mmd>
    
As you can see the two types of files are quite similar, but the template contains a number of tags from the "edt" namespace.

All tags from the edt namespace except editorDataTypes declares a named variable in the template. These variables are used to read and write the dynamic parts of the metadata, i.e the part of the metadata files that are different between each file. Each editor variable has type determined by its tag name and it also has number of attributes

  * varName (required): The name of the particular variable. This is later used in the configuration of the editor view.
  * minOccurs: The minimal number of times the variable can occur. This is used to generate an appropriate view of the variable and NOT for validation. Default unbounded
  * maxOccurs: The maximum number of times the variable can occur. This is used to generate an appropriate view of the variable and NOT for validation. Default unbounded
  * resource: The name a resource that contains the possible valid values for this variable. This is used to generate an appropriate view of the variable and NOT for validation.
  * xpath: The XPath that is used to find the XML node in the metadata file that the variable contains. This is almost always calculated for you, but in the cases that the XPath to more than one variable is the same it needs to specified manually. For instance in the example the editor cannot calculate a unique XPath the variables "title_en" and "title_no" since they have the same XPath /mmd:mmd/mmd:title.
   
The editor variables can also contain other editor variables. This is mostly used when two or more variables can be grouped into a bigger entity and at the same repeated more the once. This is the case for <mmd:data_access> in the example. <mmd:data_access> has two sub element <mmd:name> and <mmd:resource> and it can also be repeated as many times as the user would like.

When reading the metadata files the editor will populate all variables based on the data found the metadata file and when it writes the metadata it will fill the template with the values for the varibles to generate the new file. ***It is important to note that the editor does not update the existing file, it instead generates a entirely new file based on the template!*** This is important since it means that anything in the metadata file that is not mentioned in the template will be gone once the file is written once.

## An example <FORMAT>Editor.xml file

The editor configuration files is used to configure the user interface for the editor for a particular format. It is used to decide which tabs should exist, the variables displayed on each page and what user interface element should be used to display the variables.

    <?xml version="1.0" encoding="UTF-8"?>
    <ec:editor xmlns:ec="http://www.met.no/schema/metadataeditor/editorConfiguration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    
    <ec:page id="description" label="Dataset description">
    
        <ec:widget xsi:type="stringWidget" variableName="metadata_identifier" label="Unique identifier for the metadata record" size="30" maxlength="1000"  />
        <ec:widget xsi:type="stringWidget" variableName="title_en" label="Title in English" size="50" maxlength="1000"/>
        <ec:widget xsi:type="stringWidget" variableName="title_no" label="Title in Norwegian" size="50" maxlength="1000"/>
        <ec:widget xsi:type="listWidget" variableName="iso_topic_category" label="ISO Topic Category" />
            
    </ec:page>
    
    <ec:page id="extent" label="Space and time">
    
        <ec:widget xsi:type="latLonBoundingBoxWidget" variableName="geographic_extent_rectangle" label="Rectangular bounding box" />
    
    </ec:page>
    
    <ec:page id="data_access" label="Data access">
       
        <ec:widget xsi:type="containerWidget" variableName="data_access" label="WMS">
            <ec:widget xsi:type="stringWidget" variableName="data_access_name" label="Name" maxlength="1000"/>       
            <ec:widget xsi:type="uriWidget" variableName="data_access_resource" label="URL" maxlength="1000"/>
        </ec:widget>
        
    </ec:page>
    <ec:editor>

The editor configuration contains one or more <page> elements and each page element containt one or more <widget> elements.

The most important part here is the configuration of the <widget> elements since these decide how the editor will look like to the user. Each widget is connected to a single variable and will display all the values for the variable.

How the widget behaves will depend on the ***minOccurs*** and ***maxOccurs*** attributes for the variable. As long as more than minOccurs values exist for the variable, a value be removed. And as long as less than maxOccurs of the values exist for the variable more values can be added.
