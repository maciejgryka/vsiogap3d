Introduction
============

Below are functional requirements that were discussed and agreed with the customer. These requirements form success criteria and will be a base for future testing.
Requirements

ID	Func1: Ability to show a 3D representation of data
Area	Functional
Description	The application will show a 3D representaion of provided data mapped onto a model of a building. The data will contain information about sensor readings in different rooms in the building. Each set of readings for any given room will be represented as a set of icons on the room's wall and other indicators (such as lighting).

ID	Func2: Ability to pull and present data from the database in real time.
Area	Functional
Description	Data will be automatically refreshed whenever database is changed (automatic notification from the database will be sent to the device to refresh data). It will also be possible to refresh the data upon user request. Refresh process should take less than 10 seconds.

ID	Nav1: Navigation
Area	UI/Navigation
Description	User should be able to freely navigate in the 3D space. That means ability to view object from different perspectives (roatate around X and Y axes) and to zoom in/out on the specific point.
Notes 	Rotation around Z axis should not be necessary and could complicate navigation. Also, as the model will be a biulding, this will provide a feeling of "stability".

ID	Nav2: Touch-screen navigation (rotate)
Area	UI/Navigation
Description 	User will be able to navigate using the touch-screen. For instance sweeping the finger down across the screen, will rotate the model around the X-axis "towards" the screen, while sweeping a finger left will rotate teh model "left" around the Y-axis.

ID 	Nav3: Touch-screen navigation (pan)
Area 	UI/Navigation
Description 	User will be able to pan the view by tapping the arrow buttons located on the sides of the screen.

ID 	Nav4: Buttons navigation
Area 	UI/Navigation
Description 	It will also be possible to navigate (pan) using physical direction buttons. Pressing the middle button will restore model to its default view (isometric).

ID 	Nav5: Zooming in/out
Area 	UI/Navigation
Description 	Zoom in/out ability would be provided by two on-screen buttons at the bottom of the screen (see Google Maps on Android emulator). Zoom in could also be initiated by a double tap on the screen, in which case it would zoom into the point of the tap (provided it's somewhere on the model - we don't want to zoom into an empty space)

ID 	Nav6: Automatically adjusting view to the phone position
Area 	UI/Navigation
Description 	Software will display the model of the building in a "portrait" mode if the phone is held vertically and in the "landscape" mode when the phone is held horizontally. 
