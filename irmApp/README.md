# irmApp
Autors : Laure Baudoin & Marie Bogusz
Version : 19/04/2019

L'executable IRMCare se trouve dans le fichier dist.


Connexion :

Pour se connecter � l'interface m�decin : 
	login	: medecin
	pwd 	: medecin
pour se connecter � l'interface technicien :
	login	: technicien
	pxd	: technicien


Interface m�decin :

L'interface m�decin permet de consulter la liste des patients en cours d'�tude et la liste des examens � v�rifier par un m�decin.
On peut rechercher un patient dans la liste par son nom ou son pr�nom.
On peur rechercher un examen dans la liste par le nom ou le pr�nom du patient associ�.
On peut s�l�ctionner un patient dans la liste puis lui ajouter une pr�visite. Lors d'une pr�visite, le m�decin renseigne des donn�es 
consernant le patient comme la tension ou le taux d'h�mogobine puis il peut ajouter un ou plusieurs m�dicaments prit par le patient 
depuis la d�rni�re pr�visite.
On peut s�l�ctionner un examen afin de visualiser les informations li�es � l'�xamen du patient puis d'�mettre son avis sur le grade
du gliome.


Interface technicien :

L'interface m�decin permet de consulter la liste des patients en cours d'�tude.
On peut rechercher un patient dans la liste par son nom ou son pr�nom.
On peut s�l�ctionner un patient dans la liste puis lui ajouter un exmane d'IRM. Le technicien renseigne les informations donn�es par
la machine. L'examen ajout� par un technicien sera ensuite visible dans la partie "v�rification examen" de l'interface m�decin.
