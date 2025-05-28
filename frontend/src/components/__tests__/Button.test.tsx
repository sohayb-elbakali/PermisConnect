import { fireEvent, render } from "@testing-library/react-native";
import React from "react";
import Button from "../Button";

describe("Button", () => {
  it("renders the title", () => {
    const { getByText } = render(
      <Button title="Click me" onPress={() => {}} />
    );
    expect(getByText("Click me")).toBeTruthy();
  });

  it("calls onPress when pressed", () => {
    const onPressMock = jest.fn();
    const { getByText } = render(
      <Button title="Press me" onPress={onPressMock} />
    );
    fireEvent.press(getByText("Press me"));
    expect(onPressMock).toHaveBeenCalled();
  });

  it("shows loading indicator when loading", () => {
    const { getByTestId, queryByText } = render(
      <Button title="Loading" onPress={() => {}} loading />
    );
    expect(getByTestId("ActivityIndicator")).toBeTruthy();
    expect(queryByText("Loading")).toBeNull();
  });
});
