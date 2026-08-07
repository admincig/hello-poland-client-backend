-- Ujednolicenie poprawnych nazw województw do zapisu małymi literami.
-- Braki i wartości spoza listy 16 województw pozostają bez zmian.

BEGIN;

UPDATE address a
SET voivodeship = lower(trim(a.voivodeship))
WHERE EXISTS (
    SELECT 1
    FROM partner p
    WHERE p.address_id = a.id
)
  AND lower(trim(a.voivodeship)) IN (
    'dolnośląskie', 'kujawsko-pomorskie', 'lubelskie', 'lubuskie',
    'łódzkie', 'małopolskie', 'mazowieckie', 'opolskie',
    'podkarpackie', 'podlaskie', 'pomorskie', 'śląskie',
    'świętokrzyskie', 'warmińsko-mazurskie', 'wielkopolskie',
    'zachodniopomorskie'
  )
  AND a.voivodeship IS DISTINCT FROM lower(trim(a.voivodeship));

UPDATE sight
SET voivodeship = lower(trim(voivodeship))
WHERE lower(trim(voivodeship)) IN (
    'dolnośląskie', 'kujawsko-pomorskie', 'lubelskie', 'lubuskie',
    'łódzkie', 'małopolskie', 'mazowieckie', 'opolskie',
    'podkarpackie', 'podlaskie', 'pomorskie', 'śląskie',
    'świętokrzyskie', 'warmińsko-mazurskie', 'wielkopolskie',
    'zachodniopomorskie'
  )
  AND voivodeship IS DISTINCT FROM lower(trim(voivodeship));

UPDATE sightevent
SET voivodeship = lower(trim(voivodeship))
WHERE lower(trim(voivodeship)) IN (
    'dolnośląskie', 'kujawsko-pomorskie', 'lubelskie', 'lubuskie',
    'łódzkie', 'małopolskie', 'mazowieckie', 'opolskie',
    'podkarpackie', 'podlaskie', 'pomorskie', 'śląskie',
    'świętokrzyskie', 'warmińsko-mazurskie', 'wielkopolskie',
    'zachodniopomorskie'
  )
  AND voivodeship IS DISTINCT FROM lower(trim(voivodeship));

COMMIT;
