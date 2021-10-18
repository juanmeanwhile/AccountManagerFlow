# AccountManagerFlow
Explore a way to observe when a user has logged in/out

How it works:
AccountManager is the place provided by Android to store account data, as tokens. Every time a user logs in, we add the account to the AccountManager. When the user logs out, we remove it.
AccountManager offers the posibility to register a listener to be notified when there are changes in the accounts, so we use this one, wrapped with a callback flow,
to detect when a user has logged in or logged out. 
The idea is to observe this in the viewModel, where we can emit the corresponding data to the UI so we can represent the screen state. 

Pros:
 - The source of truth is the AccountManagerm we don't add anything in between.
 - The flow can be combined with others needed for that UI, even use it to trigger getting other flows which require a logged in user.
   
    val uiState = accountManager.accountStateFlow()
        .flatMapLatest { status ->
            if (status is LoginStatus.LoggedIn) {
                repo.getUserOrdersFlow(status.userId)
            } else {
               ...
            }
        }.asLiveData()
 
Cons: 
 - ?


