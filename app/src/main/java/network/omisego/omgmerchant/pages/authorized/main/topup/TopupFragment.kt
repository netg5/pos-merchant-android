package network.omisego.omgmerchant.pages.authorized.main.topup

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.pagination.PaginationList
import kotlinx.android.synthetic.main.fragment_topup.*
import network.omisego.omgmerchant.R
import network.omisego.omgmerchant.base.BaseFragment
import network.omisego.omgmerchant.databinding.FragmentTopupBinding
import network.omisego.omgmerchant.extensions.findMainFragment
import network.omisego.omgmerchant.extensions.observeFor
import network.omisego.omgmerchant.extensions.provideMainFragmentAndroidViewModel
import network.omisego.omgmerchant.extensions.provideMainFragmentViewModel
import network.omisego.omgmerchant.extensions.selectedToken
import network.omisego.omgmerchant.extensions.setError
import network.omisego.omgmerchant.extensions.setTokens
import network.omisego.omgmerchant.pages.authorized.main.MainViewModel
import network.omisego.omgmerchant.helper.HelperNumberFormatter

class TopupFragment : BaseFragment() {
    private lateinit var binding: FragmentTopupBinding
    private lateinit var viewModel: TopupViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onProvideViewModel() {
        viewModel = provideMainFragmentAndroidViewModel()
        mainViewModel = provideMainFragmentViewModel()
    }

    override fun onBindDataBinding() {
        binding.liveCalc = viewModel.liveCalculator
        binding.handler = viewModel.handler
        binding.decorator = HelperNumberFormatter()
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
    }

    override fun onObserveLiveData() {
        findMainFragment().observeFor(viewModel.liveCalculator) {
            notifyCalculatorStateChange()
        }

        findMainFragment().observeFor(viewModel.liveSelectedToken) {
            notifyCalculatorStateChange()
        }

        with(mainViewModel) {
            observeFor(liveTokenAPIResult) {
                it.handle(
                    ::handleLoadTokens,
                    spinner::setError
                )
                spinner.selectedToken = viewModel.liveSelectedToken.value
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_topup,
            container,
            false
        )
        return binding.root
    }

    private fun notifyCalculatorStateChange() {
        mainViewModel.liveEnableNext.value = viewModel.shouldEnableNextButton()
        viewModel.dispatchHelperTextState()
    }

    private fun handleLoadTokens(tokens: PaginationList<Token>) {
        spinner.setTokens(tokens, viewModel.liveSelectedToken)
    }
}
