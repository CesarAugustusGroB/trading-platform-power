# API Examples

The following sample requests assume the services run on `localhost`.

## Orderbook Service

- Place an order:
  ```bash
  curl -X POST http://localhost:8080/orders \
       -H 'Content-Type: application/json' \
       -d '{"symbol":"AAPL","price":10,"quantity":5,"type":"BUY"}'
  ```
- List all orders:
  ```bash
  curl http://localhost:8080/orders
  ```

## Trade Service

- List trades:
  ```bash
  curl http://localhost:8080/trades
  ```

## Wallet Service

- Deposit funds:
  ```bash
  curl -X POST http://localhost:8080/wallets/u1/deposit/10
  ```
- Withdraw funds:
  ```bash
  curl -X POST http://localhost:8080/wallets/u1/withdraw/5
  ```

## Portfolio Service

- Get user portfolio:
  ```bash
  curl http://localhost:8080/portfolios/u1
  ```

## Test Scenarios

- **Order placement publishes an event** (see `OrderControllerTest`)
- **Portfolio retrieval returns data from the repository** (`PortfolioControllerTest`)
- **Trade listing returns trades from the repository** (`TradeControllerTest`)
- **Wallet deposit updates the balance** (`WalletControllerTest`)
