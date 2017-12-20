# SpringPersonalBanker
Intro & Technical Features
--------------------------

'PersonalBanker' which is a sophisticated Java/JEE financial application which integrates the following frameworks: Maven, Spring 3.1 (IOC and AOP), Java Server Faces (JSF 2.0) / Primefaces 3.5, Hibernate 4.2 (with an underlying MySQL database), JAX-WS web services and some external libraries like the Yahoo Finance API and Joda.

 Sortable data tables, wizards, dialogues and various 'cool'  controls are used.   Ajax calls are heavily employed to dynamically render content on pages based on client events without round trip requests to the server.    Generally the validation logic is taken care of in Java-side code although certain JSF automatic validations are also incorporated.   There is sophisticated logic contained on the server-side within the Spring service/component POJOs.
 
What the App is about
---------------------
The PersonalBanker application provides a mock-up of an online bank with the ability to open accounts and process transactions on various account activities (e.g. stock buys/sells, savings deposits, etc). Sophistication is provided via the ability to execute inter-fund transfers, perform complex automatic interest/valuation calculations, computation of capital gains/losses on stock sells, etc. The user is provided details about each account in his/her portfolio as well as details for each historic transaction in an account.
Below is a description of each of the tabs on the main page.

Navigating the App / Usage Instructions
-----------------------------------------

View/Edit Client Settings 
-------------------------
After setting up client information (Client Setup tab) when first using the application the user then has the ability to view/edit Detail Client Info (person or business depending upon choice) and Address using the 'accordion' layout under this tab. Please note that when in edit mode the information will be subject to validations when the user attempts to save (e.g. entering valid email, entering in a non-blank city, etc).

Portfolio Information (and underlying Account Detail pages)
-----------------------------------------------------------
The screen under this tab will give a summary of open accounts in a record table (each of these accounts was created/opened by going through the wizard in the Add An Account tab – see below). Prior to adding any account (ie. when the user first starts in the system) the record grid will be empty.

To 'drill down' to a specific account, the user selects a specific account record and clicks the 'View Selected Account Detail' button.

**The Account Detail Information Page consists of**

(i) detailed account information data (e.g. Account Name, Open Date, Balance, etc.)
(ii) an Account Transaction Activity grid consisting of transactions. Initially all records are shown (ordered by transaction date) but the user can refine the search to date range and/or transaction type (and clicking the 'View' button). For refined search, running totals are suppressed. When the user clicks/selects a specific transaction record, the transaction detail is shown in a dialog and the user may be able to reverse a transaction (see criteria below).
(iii) the ability to Create a New Transaction (except for annuities and non-cashable GIC/Term Deposits) by selecting Transaction Type and clicking 'Go' button.

The types of accounts are:

*Chequing*

An account with cash holdings and no interest earned. Deposits and withdrawals can be made on this account (with the amounts coming from/going to Cash). Also cheques can be issued. Transfers can be made in/out of this account to other chequing accounts or savings accounts. Also a chequing account may be used as a source of funds when opening another account or for Stock buys. A chequing account may also a destination account for stock sells, annuity payments or cashable GIC/Term Deposit withdrawals. While back-dating of transactions is allowed, please note that validations ensure that any activity into/out of a chequing account occurs no earlier than opening date of the account and that sufficient funds are available from the source account.

*Savings*

An account with cash holdings where interest is earned. The interest rate on a savings account can vary with the ability for the user to enter in new rates with corresponding effective dates. The savings account interest rate is annual but interest is calculated on a daily basis on the day-end balance (including accrued interest) with interest debited to the account at the end of each month. Please note that any change to interest rates and any account activity result in a recalculation of interest (and new month end debits) going forward from the effective date of the change to the system date. Interest payments are also determined (and updated if necessary) at each login.

Transfers can be made in/out of this account to other savings accounts or chequing accounts. Also a savings account may be used as a source of funds when opening another account or for Stock buys. A savings account may also a destination account for stock sells, annuity payments or cashable GIC/Term Deposit withdrawals. While back-dating of transactions is allowed, please note that validations ensure that any activity into/out of a chequing account occurs no earlier than opening date of the account and that sufficient funds are available from the source account.

*GIC/Term Deposit*

An account where balance is the amount the account opened with minus (in the case of CASHABLE GIC/Term Deposit) any funds out withdrawals. The maturity value of the account (future value) is also presented in the account summary. The account has an associated interest rate with compounding option selected when the account is opened (Add An Account wizard tab). If the GIC/Term Deposit is cashable then Funds Out (to savings/chequing/cash) transactions can be created, otherwise there are no transactions after the account opening.

*Annuity*

This is an account with periodic payments made to another account (chequing/savings) or to Cash. The term, frequency of payments and interest rate (along with compounding) are determined when the user creates the account (Add an Account wizard tab). The account summary presents as the balance the present value (PV) of the remaining payments of the annuity computed given the interest, compounding, term and frequency parameters. The user cannot create new transactions although the open account and payment transactions are shown in the account activity records table (and detail can be viewed by clicking/selecting a transaction).

*Stock*

This is an account based upon a particular stock represented by a valid stock exchange symbol. The current price and market value (price * No. of Shares) is shown on the account detail with this information retrieved via a web service utilizing the Yahoo Finance API. When the account is opened (open date) and for Buy/Sell transactions, the historic/current price of the stock is obtained from the Yahoo Finance ticker and this can be applied unless the user elects to used his/her own price.

Buys/Sells have funds coming from / going to Cash or an offset chequing/savings account. In the case of buys with source of funds from chequing/savings, validation is done to ensure that there are sufficient funds in the source account. Transaction backdating (from system date) is allowed but note that for buys/sells from/to chequing/savings validations are done to ensure that the transaction occurs no earlier than the opening date of the offset chequing/savings account. Capital gains/losses are computed using the average costing method on stock sells. To preserve the integrity of capital gain/loss calculations, buys/sells/reversals are not allowed for transaction dates on/before the Stock account's last unreversed Sell transaction date. Also note that stock buys/sells are not allowed on dates when the underlying stock exchange is closed (e.g. weekends or statutory holidays).

*Note Regarding Account Transaction REVERSALS*

Reversals can be made on transaction detail dialog (button is shown/enabled) provided we are not dealing with transactions that involve the opening of an account (this account or the offsetting transaction account). Reversals are not allowed on annuity payouts, savings interest debits and stock transactions (buys/sells) prior to the last unreversed Sell transaction.

Add An Account
--------------

The user is taken to a a four-step wizard (with Next and Back buttons) whereby he/she chooses an account type and enters in information for creation of that account (the account types were discussed above). When creating an account, the source of funds for opening the account are Cash or any chequing/savings accounts that were already created. Note that each account must have an unique account name and account number. And stock accounts must correspond to a valid stock symbol (recognized by Yahoo Finance).
To escape this wizard, click the Quit button on the first screen (Account Type) or on the last screen (Confirmation). [navigation to the first screen can be done by clicking the 'Back' button]

*





