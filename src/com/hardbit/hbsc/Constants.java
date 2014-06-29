/*
 * Copyright 2011-2014 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hardbit.hbsc;


import java.nio.charset.Charset;

import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.params.MainNetParams;




public class Constants
{

	public static final NetworkParameters NETWORK_PARAMETERS = MainNetParams.get();
	private static final String FILENAME_NETWORK_SUFFIX = NETWORK_PARAMETERS.getId().equals(NetworkParameters.ID_MAINNET) ? "" : "-testnet";
	public static final String WALLET_FILENAME_PROTOBUF = "hardwarewallet" + FILENAME_NETWORK_SUFFIX;
	public static final String INI_KEY_FILENAME= "ini" + FILENAME_NETWORK_SUFFIX;	
	public static final Charset UTF_8 = Charset.forName("UTF-8");
	public static final Charset US_ASCII = Charset.forName("US-ASCII");
}
