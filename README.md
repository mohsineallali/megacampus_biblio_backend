# Gestion de Bibliotheque — Frontend

Application Angular pour la gestion de bibliotheque.

**Stack:** Angular 21 · NgModules

## Prerequis

- Node.js 20.19+ ou 22.12+ (LTS recommande; voir [versions supportees](https://angular.dev/reference/versions))
- npm
- Angular CLI 21 (optionnel, `npx ng ...` fonctionne aussi sans installation globale)

## Lancer le frontend

Dans un terminal:

```bash
npm install
npx ng serve --open
```

Frontend disponible sur:
- `http://localhost:4200`

Le frontend appelle le backend via:
- `http://localhost:8080/api`

> Assurez-vous que le backend est deja lance avant de demarrer le frontend.

## Build de production

```bash
npx ng build
```

Les fichiers generes sont dans:
- `frontend/dist/gestion-biblio`

---

Merci a notre professeur pour son encadrement et ses precieux conseils.