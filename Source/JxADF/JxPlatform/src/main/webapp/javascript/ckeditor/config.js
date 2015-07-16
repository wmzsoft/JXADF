/**
 * @license Copyright (c) 2003-2014, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	 config.disableNativeSpellChecker = false ;  
	 //把默认工具栏改为‘MyToolbar’   
	 config.toolbar_BASE =    
	  [   
			['Source','-','Preview','-','Templates'],   
			['Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo', 'Redo'],   
			['Find','Replace','-','SelectAll'],   
			['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'],   
			'/',
			['Bold','Italic','Underline','Strike','-','Subscript','Superscript','RemoveFormat'],   
			['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],   
			['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],   
			['Link','Unlink','Anchor'],   
			'/',
			['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','IFrame'],   
			['Styles','Format','Font','FontSize','lineheight'],   
			['TextColor','BGColor'],   
			['ShowBlocks','Maximize']   
	  ];   
		config.toolbar = 'BASE';
		config.image_previewText='http://osgi.jxtech.net';
};
