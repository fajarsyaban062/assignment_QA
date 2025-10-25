CREATE TABLE Lp (
    LpId SERIAL PRIMARY KEY,
    LpName VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Symbol (
    SymbolId SERIAL PRIMARY KEY,
    SymbolName VARCHAR(50) NOT NULL UNIQUE,
    coreSymbol VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE "Order" (
    OrderId SERIAL PRIMARY KEY,
    LpId INT NOT NULL,
    SymbolId INT NOT NULL,
    Direction VARCHAR(10) NOT NULL,
    Volume INT NOT NULL,

    CONSTRAINT fk_lp
        FOREIGN KEY (LpId)
        REFERENCES Lp (LpId)
        ON DELETE RESTRICT,
    
    CONSTRAINT fk_symbol
        FOREIGN KEY (SymbolId)
        REFERENCES Symbol (SymbolId)
        ON DELETE RESTRICT
);

INSERT INTO Lp (LpName)
VALUES 
    ('BCA'),
    ('Mandiri'),
    ('Jago');
   
 INSERT INTO Symbol (SymbolName, coreSymbol)
VALUES 
    ('EURUSD', 'EUR/USD'),
    ('GBPUSD', 'GBP/USD'),
    ('OIL', 'OILCash'),
    ('XAUUSD', 'XAU/USD');
   
  INSERT INTO "Order" (LpId, SymbolId, Direction, Volume)
VALUES 
    (1, 1, 'SELL', 500000),
    (2, 1, 'SELL', 500000),
    (3, 1, 'BUY', 500000),
   	(2, 2, 'BUY', 500000),
   	(1, 2, 'BUY', 250000),
    (3, 2, 'SELL', 500000),
    (1, 3, 'BUY', 250000),
   	(3, 3, 'BUY', 500000)
;

select * from lp;
select * from Symbol;
select * from "Order";

select * 
	from 
		"Order" as o
	join Symbol as s on o.symbolid = s.symbolid
	join lp as l on o.lpid = l.lpid ;

WITH SymbolNetVolume AS (
    SELECT
        s.SymbolName,
        SUM(
            CASE 
                WHEN o.Direction = 'BUY' THEN o.Volume 
                ELSE -o.Volume
            END
        ) AS net_volume
    FROM 
        "Order" o
    JOIN 
        Symbol s ON o.SymbolId = s.SymbolId
    WHERE 
        s.SymbolName = 'EURUSD'
    GROUP BY 
        s.SymbolName
)
---------------------------------------------------------------------------
SELECT
    SymbolName AS "Symbol",
    CASE 
        WHEN net_volume >= 0 THEN 'BUY' 
        ELSE 'SELL' 
    END AS "Direction",
    ABS(net_volume) AS "Total Volume"
FROM 
    SymbolNetVolume;



