------------------------------------------------------------
-- V14__views_beacons_localizacao.sql
-- Criação de views: beacons disponíveis e última localização
------------------------------------------------------------

-- View: beacons disponíveis (sem moto)
IF OBJECT_ID('VW_BEACONS_DISPONIVEIS', 'V') IS NOT NULL
    DROP VIEW VW_BEACONS_DISPONIVEIS;
GO

CREATE VIEW VW_BEACONS_DISPONIVEIS AS
SELECT b.ID_BEACON, b.UUID, b.BATERIA, b.ID_MODELO_BEACON
FROM TB_BEACON b
WHERE b.ID_MOTO IS NULL;
GO

-- View: última localização por moto
IF OBJECT_ID('VW_MOTOS_ULTIMA_LOCALIZACAO', 'V') IS NOT NULL
    DROP VIEW VW_MOTOS_ULTIMA_LOCALIZACAO;
GO

CREATE VIEW VW_MOTOS_ULTIMA_LOCALIZACAO AS
SELECT x.ID_MOTO, x.PLACA, x.POSICAO_X, x.POSICAO_Y, x.DATA_HORA, x.PATIO
FROM (
    SELECT m.ID_MOTO, m.PLACA, l.POSICAO_X, l.POSICAO_Y, l.DATA_HORA,
           p.NOME AS PATIO,
           ROW_NUMBER() OVER (PARTITION BY m.ID_MOTO ORDER BY l.DATA_HORA DESC) AS RN
    FROM TB_MOTO m
    LEFT JOIN TB_LOCALIZACAO l ON l.ID_MOTO = m.ID_MOTO
    LEFT JOIN TB_PATIO p ON p.ID_PATIO = l.ID_PATIO
) x
WHERE x.RN = 1;
GO
