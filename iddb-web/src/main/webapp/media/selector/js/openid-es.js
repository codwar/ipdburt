var providers_large = {
	google : {
		name : 'Google',
		url : 'https://www.google.com/accounts/o8/id'
	},
	yahoo : {
		name : 'Yahoo',
		url : 'http://me.yahoo.com/'
	},
};

var providers_small = {
	flickr : {
		name : 'Flickr',        
		label : 'Ingrese su usuario Flickr',
		url : 'http://flickr.com/{username}/'
	},
	blogger : {
		name : 'Blogger',
		label : 'Ingrese su cuenta Blogger',
		url : 'http://{username}.blogspot.com/'
	},
	launchpad : {
		name : 'Launchpad',
		label : 'Usuario Launchpad',
		url : 'https://launchpad.net/~{username}'
	},
};

openid.locale = 'es';
openid.sprite = 'es';
openid.demo_text = 'In client demo mode. Normally would have submitted OpenID:';
openid.signin_text = 'Iniciar sesión';
openid.image_title = 'inicar sesión usando {provider}';
