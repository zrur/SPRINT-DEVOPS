CREATE TABLE TB_LOGRADOURO (
                               ID_LOGRADOURO BIGINT IDENTITY(1,1) PRIMARY KEY,
                               NOME NVARCHAR(150) NOT NULL,
                               CEP NVARCHAR(10) NOT NULL,
                               ID_BAIRRO BIGINT NOT NULL,  -- Este pode ser BIGINT pois TB_BAIRRO.ID_BAIRRO é BIGINT

                               CONSTRAINT FK_LOGRADOURO_BAIRRO FOREIGN KEY (ID_BAIRRO)
                                   REFERENCES TB_BAIRRO(ID_BAIRRO)
);