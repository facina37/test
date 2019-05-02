# irmApp
Autors : Laure Baudoin & Marie Bogusz
Version : 19/04/2019

L'executable IRMCare se trouve dans le fichier dist.


Connexion :

Pour se connecter à l'interface médecin : 
	login	: medecin
	pwd 	: medecin
pour se connecter à l'interface technicien :
	login	: technicien
	pxd	: technicien


Interface médecin :

L'interface médecin permet de consulter la liste des patients en cours d'étude et la liste des examens à vérifier par un médecin.
On peut rechercher un patient dans la liste par son nom ou son prénom.
On peur rechercher un examen dans la liste par le nom ou le prénom du patient associé.
On peut séléctionner un patient dans la liste puis lui ajouter une prévisite. Lors d'une prévisite, le médecin renseigne des données 
consernant le patient comme la tension ou le taux d'hémogobine puis il peut ajouter un ou plusieurs médicaments prit par le patient 
depuis la dérnière prévisite.
On peut séléctionner un examen afin de visualiser les informations liées à l'éxamen du patient puis d'émettre son avis sur le grade
du gliome.


Interface technicien :

L'interface médecin permet de consulter la liste des patients en cours d'étude.
On peut rechercher un patient dans la liste par son nom ou son prénom.
On peut séléctionner un patient dans la liste puis lui ajouter un exmane d'IRM. Le technicien renseigne les informations données par
la machine. L'examen ajouté par un technicien sera ensuite visible dans la partie "vérification examen" de l'interface médecin.
